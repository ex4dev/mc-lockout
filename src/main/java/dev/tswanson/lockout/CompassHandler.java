package dev.tswanson.lockout;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.stream.Collectors;

public class CompassHandler implements Listener {

    private static final List<String> COMPASS_LORE = List.of(ChatColor.GRAY + "Use this to track other players!");

    private final Map<Player, Player> compassTargets = new HashMap<>();
    private BukkitTask task;
    private final ItemStack compassItem = new ItemStack(Material.COMPASS);
    private final HashMap<Player, Long> lastCompassChange = new HashMap<>();

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR)
            return;

        long currentTime = event.getPlayer().getWorld().getFullTime();
        if (lastCompassChange.getOrDefault(event.getPlayer(), 0L) + 10 > currentTime) return;
        lastCompassChange.put(event.getPlayer(), currentTime);
        event.getPlayer().sendMessage("Updating compass");

        ItemStack item = event.getItem();
        if (item == null) return;
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null && (!itemMeta.hasLore() || !itemMeta.getLore().equals(COMPASS_LORE)))
            return;

        ArrayList<Player> players = Bukkit.getOnlinePlayers()
                .stream()
                .sorted(Comparator.comparing((Player a) -> a.getName()))
                .filter((player) -> !player.equals(event.getPlayer()))
                .collect(Collectors.toCollection(ArrayList::new));

        if (players.isEmpty()) return;

        if (compassTargets.containsKey(event.getPlayer())) {
            int newIndex = players.indexOf(compassTargets.get(event.getPlayer())) + 1;
            if (newIndex >= players.size()) {
                newIndex = 0;
            }
            track(event.getPlayer(), players.get(newIndex));
        } else {
            track(event.getPlayer(), players.get(0));
        }
    }

    void track(Player player, Player toTrack) {
        compassTargets.put(player, toTrack);
        player.sendMessage(ChatColor.GREEN + "You are now tracking " + toTrack.getDisplayName() + ChatColor.GREEN + ".");
        updateCompass(player);
    }

    @EventHandler
    public void onPlayerSpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Player player) {
            updateCompass(player);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        ItemMeta itemMeta = event.getItemDrop().getItemStack().getItemMeta();
        if (itemMeta != null && itemMeta.hasLore() && itemMeta.getLore().equals(COMPASS_LORE)) {
            event.setCancelled(true);
        }
    }

    public void initialize() {
        this.task = Bukkit.getScheduler().runTaskTimer(Lockout.getInstance(), () -> {
            // Twice per second, update players' compasses
            Bukkit.getOnlinePlayers().forEach(this::updateCompass);
        }, 0L, 10L);
    }

    public void stop() {
        if (this.task != null) {
            this.task.cancel();
            this.task = null;
        }
    }

    private void updateCompass(Player player) {
        ItemStack compass = compassItem.clone();
        ItemMeta meta = compass.getItemMeta();
        assert meta != null;

        if (compassTargets.containsKey(player)) {
            meta.setDisplayName(ChatColor.GREEN + "Tracking " + compassTargets.get(player).getDisplayName());
        } else {
            meta.setDisplayName(ChatColor.RED + "Inactive compass" + ChatColor.GRAY + " (right click to start tracking)");
        }

        meta.setLore(COMPASS_LORE);
        compass.setItemMeta(meta);

        // Replace compass in inventory with new one
        boolean hasCompass = false;
        for (int i = 0; i < 35; i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item == null) continue;
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta == null) continue;
            List<String> lore = itemMeta.getLore();
            if (lore == null) continue;
            if (lore.equals(COMPASS_LORE)) {
                hasCompass = true;
                player.getInventory().setItem(i, compass);
            }
        }

        // prevent player from receiving extra compass upon dragging compass in inventory
        ItemStack cursorItem = player.getItemOnCursor();
        if (!hasCompass) {
            if (!cursorItem.hasItemMeta() || !cursorItem.getItemMeta().hasLore() || !cursorItem.getItemMeta().getLore().equals(COMPASS_LORE)) {
                player.getInventory().addItem(compass);
            }
        }
        if (compassTargets.containsKey(player)) {
            player.setCompassTarget(compassTargets.get(player).getLocation());
        }
    }
}
