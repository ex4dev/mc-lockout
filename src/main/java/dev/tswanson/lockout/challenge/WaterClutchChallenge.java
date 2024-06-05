package dev.tswanson.lockout.challenge;

import dev.tswanson.lockout.Challenge;
import dev.tswanson.lockout.Lockout;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

// Become a candidate by placing water
// Succeed by landing inside the water you placed if fall distance > minFallDistance
// Become not a candidate after like a second or two

public final class WaterClutchChallenge implements Challenge {
    private final ChallengeMetadata metadata;
    private final float minFallDistance;

    private final HashMap<Player, Float> lastFallDistance = new HashMap<>();
    private final HashMap<Player, Location> waterPlacement = new HashMap<>();

    public WaterClutchChallenge(ChallengeMetadata metadata, float minFallDistance) {
        this.metadata = metadata;
        this.minFallDistance = minFallDistance;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Block block = event.getTo().getBlock();
        float distance = lastFallDistance.getOrDefault(event.getPlayer(), 0f);
        if (block.getLocation().equals(waterPlacement.get(player)) && distance >= minFallDistance) {
            markCompleted(event.getPlayer());
        }
        lastFallDistance.put(player, player.getFallDistance());
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (event.getBucket() != Material.WATER_BUCKET) return;
        Player player = event.getPlayer();
        Block waterBlock = event.getBlockClicked().getRelative(event.getBlockFace());
        waterPlacement.put(player, waterBlock.getLocation());
        new BukkitRunnable() {
            @Override
            public void run() {
                waterPlacement.remove(player);
            }
        }.runTaskLater(Lockout.getInstance(), 20L); // You have 20 ticks to land in the water you just placed
    }

    @Override
    public ChallengeMetadata metadata() {
        return metadata;
    }

    public float minFallDistance() {
        return minFallDistance;
    }
}
