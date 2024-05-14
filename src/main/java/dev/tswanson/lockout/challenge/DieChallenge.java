package dev.tswanson.lockout.challenge;

import dev.tswanson.lockout.Challenge;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public record DieChallenge(ChallengeMetadata metadata, DamageType damageType, EntityType damagingEntityType) implements Challenge {

    public DieChallenge(ChallengeMetadata metadata, DamageType damageType) {
        this(metadata, damageType, null);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        boolean damageTypeMatches = player.getLastDamageCause().getDamageSource().getDamageType() == this.damageType;
        boolean entityTypeMatches = this.damagingEntityType == null || player.getLastDamageCause().getEntityType() == this.damagingEntityType;
        if (damageTypeMatches && entityTypeMatches) {
            markCompleted(player);
        }
    }
}
