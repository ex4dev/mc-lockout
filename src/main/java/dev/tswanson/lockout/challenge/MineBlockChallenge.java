package dev.tswanson.lockout.challenge;

import dev.tswanson.lockout.Challenge;
import dev.tswanson.lockout.gui.Icon;
import dev.tswanson.lockout.gui.StaticIcon;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class MineBlockChallenge implements Challenge {

    private final Icon icon;
    private final Material targetBlock;
    private final String name;

    public MineBlockChallenge(StaticIcon icon, String name) {
        this.icon = icon;
        this.name = name;
        this.targetBlock = icon.getMaterial();
    }

    public MineBlockChallenge(Icon icon, String name, Material targetBlock) {
        this.icon = icon;
        this.name = name;
        this.targetBlock = targetBlock;
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
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType().equals(this.targetBlock)) {
            markCompleted(event.getPlayer());
        }
    }
}
