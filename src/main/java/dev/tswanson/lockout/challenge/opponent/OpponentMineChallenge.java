package dev.tswanson.lockout.challenge.opponent;

import dev.tswanson.lockout.gui.Icon;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public record OpponentMineChallenge(Icon icon, String name, Material material) implements OpponentChallenge {
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() != material) return;
        markFailed(event.getPlayer());
    }
}
