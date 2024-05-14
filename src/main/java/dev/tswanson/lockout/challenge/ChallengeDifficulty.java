package dev.tswanson.lockout.challenge;

import org.bukkit.ChatColor;

public enum ChallengeDifficulty {
    EASY(ChatColor.GREEN), MEDIUM(ChatColor.YELLOW), HARD(ChatColor.RED), EXTREME(ChatColor.DARK_RED);

    public final ChatColor color;

    ChallengeDifficulty(ChatColor color) {
        this.color = color;
    }

    public static ChallengeDifficulty fromRow(int row) {
        return switch (row) {
            case 0 -> EASY;
            case 1, 2 -> MEDIUM;
            case 3 -> HARD;
            case 4 -> EXTREME;
            default -> null;
        };
    }
}