package dev.tswanson.lockout.challenge.item;

import dev.tswanson.lockout.Challenge;
import dev.tswanson.lockout.Lockout;
import dev.tswanson.lockout.gui.Icon;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;

import java.util.Arrays;
import java.util.stream.Stream;

public class ObtainItemsChallenge implements Challenge {
    private final Icon icon;
    private final String name;
    private final Material[] items;
    private final GoalType goalType;

    public enum GoalType {
        ALL, ANY
    }

    public ObtainItemsChallenge(Icon icon, String name, Material... items) {
        this.icon = icon;
        this.name = name;
        this.items = items;
        this.goalType = GoalType.ALL;
    }

    public ObtainItemsChallenge(Icon icon, String name, GoalType goalType, Material... items) {
        this.icon = icon;
        this.name = name;
        this.items = items;
        this.goalType = goalType;
    }

    @Override
    public Icon getIcon() {
        return this.icon;
    }

    @Override
    public String getName() {
        return this.name;
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
