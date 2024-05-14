package dev.tswanson.lockout.challenge.item;

import dev.tswanson.lockout.Challenge;
import dev.tswanson.lockout.Lockout;
import dev.tswanson.lockout.challenge.ChallengeMetadata;
import dev.tswanson.lockout.gui.Icon;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;

import java.util.Arrays;
import java.util.stream.Stream;

public record ObtainItemsChallenge(ChallengeMetadata metadata, GoalType goalType, Material... items) implements Challenge {

    public enum GoalType {
        ALL, ANY
    }

    public ObtainItemsChallenge(ChallengeMetadata metadata, Material... items) {
        this(metadata, GoalType.ALL, items);
    }

    @EventHandler
    public void onItemObtained(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player) {
            update(player);
        }
    }

    @EventHandler
    public void onInventoryUpdate(InventoryPickupItemEvent event) {
        // TODO - not sure what this event does exactly
        if (event.getInventory().getHolder() instanceof Player player) {
            update(player);
        }
    }

    @Override
    public void initialize() {
        // Update players every second just in case the events didn't fire
        Bukkit.getScheduler().runTaskTimer(Lockout.getInstance(), () -> {
            Bukkit.getOnlinePlayers().forEach(this::update);
        }, 0L, 20L);
    }

    private void update(Player player) {
        Stream<Material> stream = Arrays.stream(items);

        boolean isCompleted = false;
        if (goalType == GoalType.ALL) {
            isCompleted = stream.allMatch((requiredItem) -> player.getInventory().contains(requiredItem));
        } else if (goalType == GoalType.ANY) {
            isCompleted = stream.anyMatch((requiredItem) -> player.getInventory().contains(requiredItem));
        }

        if (isCompleted) {
            markCompleted(player);
        }
    }
}
