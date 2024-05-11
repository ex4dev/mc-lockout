package dev.tswanson.lockout.challenge.opponent;

import dev.tswanson.lockout.gui.Icon;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public record OpponentDieChallenge(Icon icon, String name) implements OpponentChallenge {
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        markFailed(event.getEntity());
    }
}
