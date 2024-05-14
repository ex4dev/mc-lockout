package dev.tswanson.lockout.challenge;

import dev.tswanson.lockout.gui.StaticIcon;

public record ChallengeMetadata(ChallengeDifficulty difficulty, StaticIcon icon, String name) {
    public ChallengeMetadata(ChallengeDifficulty difficulty, StaticIcon icon, String name) {
        this.difficulty = difficulty;
        this.icon = icon;
        this.name = difficulty.color + name;
    }

    public static ChallengeMetadata of(ChallengeDifficulty difficulty, StaticIcon icon, String name) {
        return new ChallengeMetadata(difficulty, icon, name);
    }
}
