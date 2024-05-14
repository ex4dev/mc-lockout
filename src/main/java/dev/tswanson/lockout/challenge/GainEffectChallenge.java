package dev.tswanson.lockout.challenge;

import dev.tswanson.lockout.Challenge;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

public record GainEffectChallenge(ChallengeMetadata metadata, PotionEffectType effectType) implements Challenge {

    @EventHandler
    public void onEffect(EntityPotionEffectEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getOldEffect() == null && event.getNewEffect().getType() == effectType) {
                markCompleted(player);
            }
        }
    }
}
