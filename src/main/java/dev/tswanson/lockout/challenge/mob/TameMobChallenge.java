package dev.tswanson.lockout.challenge.mob;

import dev.tswanson.lockout.Challenge;
import dev.tswanson.lockout.gui.Icon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTameEvent;

public class TameMobChallenge implements Challenge {

    private final Icon icon;
    private final String name;
    private final EntityType entityType;

    public TameMobChallenge(Icon icon, String name, EntityType entityType) {
        this.icon = icon;
        this.name = name;
        this.entityType = entityType;
    }

    @Override
    public Icon icon() {
        return icon;
    }

    @Override
    public String name() {
        return name;
    }

    @EventHandler
    public void onPlayerInteract(EntityTameEvent event) {
        if (event.getOwner() instanceof Player player) {
            if (event.getEntityType() == entityType) {
                markCompleted(player);
            }
        }
    }
}
