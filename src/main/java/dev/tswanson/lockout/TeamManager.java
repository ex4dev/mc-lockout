package dev.tswanson.lockout;

import com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class TeamManager {

    private static Map<ChatColor, String> randomTeamNames = Map.of(
        ChatColor.RED, "Red",
        ChatColor.BLUE, "Blue",
        ChatColor.GREEN, "Green",
        ChatColor.YELLOW, "Yellow",
        ChatColor.LIGHT_PURPLE, "Pink",
        ChatColor.WHITE, "White"
    );

    private final ScoreboardManager manager = Bukkit.getScoreboardManager();
    private Scoreboard board = manager.getMainScoreboard();
    private final HashMap<Team, Collection<Challenge>> completedChallenges = new HashMap<>();
    private final HashMap<Team, Collection<Challenge>> failedChallenges = new HashMap<>();

    public Scoreboard getScoreboard() {
        return board;
    }

    /**
     * Creates a new team and adds it to the scoreboard.
     */
    public Team createTeam(String name, ChatColor color) {
        Team team = board.registerNewTeam(name);
        team.setColor(color);
        completedChallenges.put(team, new HashSet<>());
        failedChallenges.put(team, new HashSet<>());
        return team;
    }

    /**
     * Creates a specific number of teams with default names (red, blue, etc.) and adds them to the scoreboard.
     */
    public void createTeams(int teamCount) {
        Set<Map.Entry<ChatColor, String>> teamNames = randomTeamNames.entrySet();
        var iterator = teamNames.iterator();
        for (int i = 0; i < teamCount; i++) {
            Map.Entry<ChatColor, String> team = iterator.next();
            createTeam(team.getValue(), team.getKey());
        }
    }

    /**
     * Assigns all online players to an already created team.
     */
    public void assignRandomTeams() {
        ArrayList<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(players);
        Iterator<Team> i = Iterables.cycle(board.getTeams()).iterator();
        for (Player player : players) {
            Team team = i.next();
            team.addEntry(player.getName());
            player.sendMessage(ChatColor.GREEN + "You are on " + team.getColor() + team.getName());
        }
    }

    public Team getPlayerTeam(Player player) {
        return board.getEntryTeam(player.getName());
    }

    public Set<Team> getTeams() {
        return board.getTeams();
    }

    public void markCompleted(Team team, Challenge challenge) {
        completedChallenges.get(team).add(challenge);
    }

    public void markCompleted(Player player, Challenge challenge) {
        markCompleted(getPlayerTeam(player), challenge);
    }

    public void markFailed(Team team, Challenge challenge) {
        failedChallenges.get(team).add(challenge);
    }

    /**
     * Returns true if the challenge is completed by *anyone*.
     */
    public boolean isCompleted(Challenge challenge) {
        for (Team team : getTeams()) {
            if (isCompleted(team, challenge)) return true;
        }
        return false;
    }

    public boolean isCompleted(Team team, Challenge challenge) {
        return completedChallenges.containsKey(team) && completedChallenges.get(team).contains(challenge);
    }

    public Team getCompletingTeam(Challenge challenge) {
        return completedChallenges.keySet().stream().filter((team) -> completedChallenges.get(team).contains(challenge)).findFirst().orElse(null);
    }

    public boolean isFailed(Team team, Challenge challenge) {
        return failedChallenges.containsKey(team) && failedChallenges.get(team).contains(challenge);
    }

    public Set<Team> getFailingTeams(Challenge challenge) {
        Set<Team> teams = new HashSet<>();
        for (Map.Entry<Team, Collection<Challenge>> c : failedChallenges.entrySet()) {
            if (c.getValue().contains(challenge)) {
                teams.add(c.getKey());
            }
        }
        return teams;
    }

    /**
     * Returns true if the specified player's team has completed the specified challenge.
     */
    public boolean isCompleted(Player player, Challenge challenge) {
        return isCompleted(getPlayerTeam(player), challenge);
    }

    public boolean isFailed(Player player, Challenge challenge) {
        return isFailed(getPlayerTeam(player), challenge);
    }

    public Collection<Challenge> getCompletedChallenges(Team team) {
        return completedChallenges.get(team);
    }

    /**
     * Remove all teams and objectives from the main scoreboard
     */
    public void clear() {
        for (Team team : board.getTeams()) {
            team.unregister();
        }
        for (Objective objective : board.getObjectives()) {
            objective.unregister();
        }
        completedChallenges.clear();
        failedChallenges.clear();
    }
}
