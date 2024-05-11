package dev.tswanson.lockout.challenge.opponent;

import dev.tswanson.lockout.gui.Icon;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

public record OpponentBuildChallenge(Icon icon, String name, Material material) implements OpponentChallenge {
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType() != material) return;
        markFailed(event.getPlayer());
    }
}
