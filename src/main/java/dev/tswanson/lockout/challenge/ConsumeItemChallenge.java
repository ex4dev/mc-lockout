package dev.tswanson.lockout.challenge;

import dev.tswanson.lockout.Challenge;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public record ConsumeItemChallenge(ChallengeMetadata metadata, Material itemType) implements Challenge {

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == itemType) {
            markCompleted(event.getPlayer());
        }
    }
}
