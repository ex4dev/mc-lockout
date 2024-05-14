package dev.tswanson.lockout.challenge;

import dev.tswanson.lockout.Challenge;
import dev.tswanson.lockout.Lockout;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scoreboard.Team;

public record KillEnemyPlayerChallenge(ChallengeMetadata metadata) implements Challenge {

    @EventHandler
    public void onPlayerKilled(PlayerDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null) return;
        Team killerTeam = Lockout.getInstance().getTeamManager().getPlayerTeam(killer);
        Team victimTeam = Lockout.getInstance().getTeamManager().getPlayerTeam(event.getEntity());
        if (!killerTeam.equals(victimTeam)) {
            markCompleted(killer);
        }
    }
}
