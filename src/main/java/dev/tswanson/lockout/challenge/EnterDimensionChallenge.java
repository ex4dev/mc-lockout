package dev.tswanson.lockout.challenge;

import dev.tswanson.lockout.Challenge;
import dev.tswanson.lockout.gui.Icon;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class EnterDimensionChallenge implements Challenge {
    private final Icon icon;
    private final String name;
    private final World.Environment environment;

    public EnterDimensionChallenge(Icon icon, String name, World.Environment environment) {
        this.icon = icon;
        this.name = name;
        this.environment = environment;
    }

    @Override
    public Icon icon() {
        return this.icon;
    }

    @Override
    public String name() {
        return this.name;
    }

    @EventHandler
    public void onDimensionChange(PlayerChangedWorldEvent event) {
        if (event.getPlayer().getWorld().getEnvironment() == environment) {
            markCompleted(event.getPlayer());
        }
    }
}
