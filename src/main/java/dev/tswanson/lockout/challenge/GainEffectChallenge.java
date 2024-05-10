package dev.tswanson.lockout.challenge;

import dev.tswanson.lockout.Challenge;
import dev.tswanson.lockout.gui.Icon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

public class GainEffectChallenge implements Challenge {
    private final String name;
    private final Icon icon;
    private final PotionEffectType effectType;

    public GainEffectChallenge(Icon icon, String name, PotionEffectType effectType) {
        this.name = name;
        this.icon = icon;
        this.effectType = effectType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Icon getIcon() {
        return icon;
    }

    @EventHandler
    public void onEffect(EntityPotionEffectEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getOldEffect() == null && event.getNewEffect().getType() == effectType) {
                markCompleted(player);
            }
        }
    }
}
