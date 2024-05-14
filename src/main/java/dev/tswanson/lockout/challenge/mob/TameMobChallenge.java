package dev.tswanson.lockout.challenge.mob;

import dev.tswanson.lockout.Challenge;
import dev.tswanson.lockout.challenge.ChallengeMetadata;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTameEvent;

public record TameMobChallenge(ChallengeMetadata metadata, EntityType entityType) implements Challenge {

    @EventHandler
    public void onPlayerInteract(EntityTameEvent event) {
        if (event.getOwner() instanceof Player player) {
            if (event.getEntityType() == entityType) {
                markCompleted(player);
            }
        }
    }
}
