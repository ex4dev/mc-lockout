package dev.tswanson.lockout.challenge.opponent;

import dev.tswanson.lockout.challenge.ChallengeMetadata;
import dev.tswanson.lockout.gui.Icon;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public record OpponentDieChallenge(ChallengeMetadata metadata) implements OpponentChallenge {
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        markFailed(event.getEntity());
    }
}
