package dev.tswanson.lockout.gui;

import org.bukkit.Material;

public class StaticIcon implements Icon {
    private final Material icon;

    public StaticIcon(Material icon) {
        this.icon = icon;
    }

    @Override
    public Material getMaterial() {
        return icon;
    }

    public static StaticIcon of(Material icon) {
        return new StaticIcon(icon);
    }
}
