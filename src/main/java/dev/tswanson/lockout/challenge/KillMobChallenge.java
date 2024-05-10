package dev.tswanson.lockout.challenge;

import dev.tswanson.lockout.Challenge;
import dev.tswanson.lockout.gui.Icon;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

public class KillMobChallenge implements Challenge {
    private final Icon icon;
    private final String name;
    private final EntityType entityType;

    public KillMobChallenge(Icon icon, String name, EntityType mobType) {
        this.icon = icon;
        this.name = name;
        this.entityType = mobType;
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
    public void onMobKilled(EntityDeathEvent event) {
        if (event.getEntityType() == this.entityType) {
            if (event.getEntity().getKiller() != null) {
                markCompleted(event.getEntity().getKiller());
            }
        }
    }
}
