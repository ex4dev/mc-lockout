package dev.tswanson.lockout.challenge;

import dev.tswanson.lockout.Challenge;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public record EnterDimensionChallenge(ChallengeMetadata metadata, World.Environment environment) implements Challenge {

    @EventHandler
    public void onDimensionChange(PlayerChangedWorldEvent event) {
        if (event.getPlayer().getWorld().getEnvironment() == environment) {
            markCompleted(event.getPlayer());
        }
    }
}
