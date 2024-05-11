package dev.tswanson.lockout.challenge.opponent;

import com.google.common.collect.MoreCollectors;
import dev.tswanson.lockout.Challenge;
import dev.tswanson.lockout.Lockout;
import dev.tswanson.lockout.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.NoSuchElementException;

/**
 * Simliar to `Challenge`, but when all except one team has failed a challenge,
 * the final team is automatically given completion.
 * Useful for "Be the last team to ..." challenges
 */
public interface OpponentChallenge extends Challenge {
    @Override
    default void markFailed(Player failingPlayer) {
        if (!Lockout.getInstance().isIngame()) return;
        if (!Lockout.getInstance().getBoardGenerator().containsChallenge(this)) return;
        TeamManager teamManager = Lockout.getInstance().getTeamManager();
        Team failingTeam = Lockout.getInstance().getTeamManager().getPlayerTeam(failingPlayer);
        if (failingTeam == null) return;
        if (teamManager.isFailed(failingTeam, this)) return;
        teamManager.markFailed(failingTeam, this);

        try {
            Team lastRemainingTeam = teamManager.getTeams().stream()
                    .filter(t -> !teamManager.getFailingTeams(this).contains(t))
                    .collect(MoreCollectors.onlyElement());
            // Only one team has not failed - they are the winner!
            teamManager.markCompleted(lastRemainingTeam, this);
            Lockout.getInstance().getMenu().update();

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (Lockout.getInstance().getTeamManager().getPlayerTeam(p).equals(lastRemainingTeam)) {
                    p.sendMessage(lastRemainingTeam.getColor() + lastRemainingTeam.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + " has completed a challenge after " + failingTeam.getColor() + failingTeam.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + " failed: " + ChatColor.YELLOW + this.name());
                    p.playSound(p, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                } else {
                    p.sendMessage(lastRemainingTeam.getColor() + lastRemainingTeam.getDisplayName() + ChatColor.RESET + ChatColor.RED + " has completed a challenge after " + failingTeam.getColor() + failingTeam.getDisplayName() + ChatColor.RESET + ChatColor.RED + " failed: " + ChatColor.YELLOW + this.name());
                    p.playSound(p, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
                }
            }

        } catch (IllegalArgumentException e) {
            // Number of teams left != 1
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (Lockout.getInstance().getTeamManager().getPlayerTeam(p).equals(failingTeam)) {
                    p.sendMessage(failingPlayer.getDisplayName() + ChatColor.RESET + ChatColor.RED + " has failed a challenge for " + failingTeam.getColor() + failingTeam.getDisplayName() + ChatColor.RESET + ChatColor.RED + ": " + ChatColor.YELLOW + this.name());
                    p.playSound(p, Sound.BLOCK_BEACON_DEACTIVATE, 1.0f, 1.0f);
                } else {
                    p.sendMessage(failingPlayer.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + " has failed a challenge for " + failingTeam.getColor() + failingTeam.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + ": " + ChatColor.YELLOW + this.name());
                }
            }
        } catch (NoSuchElementException ignored) {}
    }
}
