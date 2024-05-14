package dev.tswanson.lockout.challenge;

import dev.tswanson.lockout.Challenge;
import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public record EnterBiomeChallenge(ChallengeMetadata metadata, Biome biome) implements Challenge {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Biome fromBiome = event.getFrom().getBlock().getBiome();
        Biome toBiome = event.getTo().getBlock().getBiome();
        if (fromBiome != toBiome && event.getPlayer().getLocation().getBlock().getBiome() == biome) {
            markCompleted(event.getPlayer());
        }
    }
}
