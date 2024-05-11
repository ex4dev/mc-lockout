package dev.tswanson.lockout;

import dev.tswanson.lockout.gui.Icon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scoreboard.Team;

public class KillEnemyPlayerChallenge implements Challenge {
    private final Icon icon;
    private final String name;

    public KillEnemyPlayerChallenge(Icon icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    @Override
    public Icon icon() {
        return this.icon;
    }

    @Override
    public String name() {
        return this.name;
    }

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
