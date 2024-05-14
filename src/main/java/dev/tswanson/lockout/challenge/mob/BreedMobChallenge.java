package dev.tswanson.lockout.challenge.mob;

import dev.tswanson.lockout.Challenge;
import dev.tswanson.lockout.challenge.ChallengeMetadata;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityBreedEvent;

public record BreedMobChallenge(ChallengeMetadata metadata, EntityType entityType) implements Challenge {

    @EventHandler
    public void onPlayerInteract(EntityBreedEvent event) {
        if (event.getBreeder() instanceof Player player) {
            if (event.getEntityType() == entityType) {
                markCompleted(player);
            }
        }
    }
}
