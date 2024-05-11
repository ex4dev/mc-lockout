package dev.tswanson.lockout.challenge.mob;

import dev.tswanson.lockout.Challenge;
import dev.tswanson.lockout.gui.Icon;
import dev.tswanson.lockout.gui.StaticIcon;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

public class KillColoredSheepChallenge implements Challenge {
    @Override
    public Icon icon() {
        return new StaticIcon(Material.LIGHT_BLUE_WOOL);
    }

    @Override
    public String name() {
        return "Kill a Colored Sheep";
    }

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
