package dev.tswanson.lockout.challenge;

import dev.tswanson.lockout.Challenge;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Collection;
import java.util.Set;

public record MineBlockChallenge(ChallengeMetadata metadata, Collection<Material> targetBlock) implements Challenge {

    public MineBlockChallenge(ChallengeMetadata metadata) {
        this(metadata, metadata.icon().getMaterial());
    }

    public MineBlockChallenge(ChallengeMetadata metadata, Material... targetBlock) {
        this(metadata, Set.of(targetBlock));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (this.targetBlock.contains(event.getBlock().getType())) {
            markCompleted(event.getPlayer());
        }
    }
}
