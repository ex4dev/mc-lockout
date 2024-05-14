package dev.tswanson.lockout.challenge.opponent;

import dev.tswanson.lockout.challenge.ChallengeMetadata;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public record OpponentDamageChallenge(ChallengeMetadata metadata, EntityDamageEvent.DamageCause damageCause) implements OpponentChallenge {
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        Player player = (Player) event.getEntity();
        markFailed(player);
    }
}
