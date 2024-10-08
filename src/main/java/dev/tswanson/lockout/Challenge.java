package dev.tswanson.lockout;

import dev.tswanson.lockout.challenge.ChallengeMetadata;
import dev.tswanson.lockout.gui.Icon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Team;

public interface Challenge extends Listener {

    ChallengeMetadata metadata();

    default void initialize() {

    }

    default void markCompleted(Player completingPlayer) {
        if (!Lockout.getInstance().isIngame()) return;
        if (!Lockout.getInstance().getBoardGenerator().containsChallenge(this)) return;
        Team team = Lockout.getInstance().getTeamManager().getPlayerTeam(completingPlayer);
        if (team == null) return;
        if (Lockout.getInstance().getTeamManager().isCompleted(this)) return;
        Lockout.getInstance().getTeamManager().markCompleted(team, this);
        Lockout.getInstance().getMenu().update();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (Lockout.getInstance().getTeamManager().getPlayerTeam(p).equals(team)) {
                p.sendMessage(completingPlayer.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + " has completed a challenge for " + team.getColor() + team.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + ": " + ChatColor.YELLOW + this.metadata().name());
                p.playSound(p, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
            } else {
                p.sendMessage(completingPlayer.getDisplayName() + ChatColor.RESET + ChatColor.RED + " has completed a challenge for " + team.getColor() + team.getDisplayName() + ChatColor.RESET + ChatColor.RED + ": " + ChatColor.YELLOW + this.metadata().name());
                p.playSound(p, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
            }
        }

        Bukkit.getConsoleSender().sendMessage(completingPlayer.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + " has completed a challenge for " + team.getColor() + team.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + ": " + ChatColor.YELLOW + this.metadata().name());
    }

    default void markFailed(Player failingPlayer) {
        if (!Lockout.getInstance().isIngame()) return;
        if (!Lockout.getInstance().getBoardGenerator().containsChallenge(this)) return;
        Team team = Lockout.getInstance().getTeamManager().getPlayerTeam(failingPlayer);
        if (team == null) return;
        Lockout.getInstance().getTeamManager().markFailed(team, this);
    }
}
