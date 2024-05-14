package dev.tswanson.lockout.challenge.mob;

import dev.tswanson.lockout.Challenge;
import dev.tswanson.lockout.challenge.ChallengeMetadata;
import org.bukkit.DyeColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

public record KillColoredSheepChallenge(ChallengeMetadata metadata) implements Challenge {

    @EventHandler
    public void onMobKilled(EntityDeathEvent event) {
        if (event.getEntityType() == EntityType.SHEEP) {
            Sheep entitySheep = (Sheep) event.getEntity();
            if (entitySheep.getColor() != null && !entitySheep.getColor().equals(DyeColor.WHITE)) {
                if (entitySheep.getKiller() != null) {
                    markCompleted(entitySheep.getKiller());
                }
            }
        }
    }
}
