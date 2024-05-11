package dev.tswanson.lockout.challenge;

import dev.tswanson.lockout.Challenge;
import dev.tswanson.lockout.gui.Icon;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class ConsumeItemChallenge implements Challenge {

    private final Icon icon;
    private final Material itemType;
    private final String name;

    public ConsumeItemChallenge(Icon icon, String name, Material itemType) {
        this.icon = icon;
        this.name = name;
        this.itemType = itemType;
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
    public void onItemConsume(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == itemType) {
            markCompleted(event.getPlayer());
        }
    }
}
