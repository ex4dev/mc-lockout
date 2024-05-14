package dev.tswanson.lockout.challenge.opponent;

import dev.tswanson.lockout.Lockout;
import dev.tswanson.lockout.challenge.ChallengeMetadata;
import dev.tswanson.lockout.gui.Icon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

public record OpponentOpenChestChallenge(ChallengeMetadata metadata) implements OpponentChallenge {
    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        if (event.getInventory().equals(Lockout.getInstance().getMenu().getInventory())) return; // TODO better way to do this?
        if (event.getInventory().getType() != InventoryType.CHEST) return;
        if (event.getPlayer() instanceof Player p) markFailed(p);
    }
}
