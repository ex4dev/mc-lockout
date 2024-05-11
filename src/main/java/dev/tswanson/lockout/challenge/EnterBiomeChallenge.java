package dev.tswanson.lockout.challenge;

import dev.tswanson.lockout.Challenge;
import dev.tswanson.lockout.gui.Icon;
import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class EnterBiomeChallenge implements Challenge {

    private final Icon icon;
    private final Biome biome;
    private final String name;

    public EnterBiomeChallenge(Icon icon, String name, Biome biome) {
        this.icon = icon;
        this.name = name;
        this.biome = biome;
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
    public void onMove(PlayerMoveEvent event) {
        Biome fromBiome = event.getFrom().getBlock().getBiome();
        Biome toBiome = event.getTo().getBlock().getBiome();
        if (fromBiome != toBiome && event.getPlayer().getLocation().getBlock().getBiome() == biome) {
            markCompleted(event.getPlayer());
        }
    }
}
