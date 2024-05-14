package dev.tswanson.lockout.challenge;

import dev.tswanson.lockout.Challenge;
import dev.tswanson.lockout.gui.Icon;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

public record KillMobChallenge(ChallengeMetadata metadata, EntityType mobType) implements Challenge {

    @EventHandler
    public void onMobKilled(EntityDeathEvent event) {
        if (event.getEntityType() == this.mobType) {
            if (event.getEntity().getKiller() != null) {
                markCompleted(event.getEntity().getKiller());
            }
        }
    }
}
