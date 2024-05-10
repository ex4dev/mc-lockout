package dev.tswanson.lockout.challenge;

import dev.tswanson.lockout.Challenge;
import dev.tswanson.lockout.gui.Icon;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DieChallenge implements Challenge {

    private final Icon icon;
    private final String name;
    private final DamageType damageType;
    private final EntityType damagingEntityType;

    public DieChallenge(Icon icon, String name, DamageType damageType, EntityType damagingEntityType) {
        this.icon = icon;
        this.name = name;
        this.damageType = damageType;
        this.damagingEntityType = damagingEntityType;
    }

    public DieChallenge(Icon icon, String name, DamageType damageType) {
        this(icon, name, damageType, null);
    }

    @Override
    public Icon getIcon() {
        return this.icon;
    }

    @Override
    public String getName() {
        return this.name;
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
