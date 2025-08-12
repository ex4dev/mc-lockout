package dev.tswanson.lockout;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Random;

public class SpawnHelper {
    private static final double SPAWN_RADIUS = 30;

    private static Random random = new Random();

    public static Location getRandomSpawnpoint(World world) {
        return getRandomSpawnpoint(world, SPAWN_RADIUS);
    }

    private static Location getRandomSpawnpoint(World world, double radius) {
        double theta = (2 * Math.PI) + (2 * Math.PI) * random.nextDouble();
        double x = radius * Math.cos(theta);
        double z = radius * Math.sin(theta);
        Block highestBlock = world.getHighestBlockAt(new Location(world, x, 0, z));
        if (highestBlock.isLiquid()) return getRandomSpawnpoint(world, radius + 1);
        return highestBlock.getLocation().add(0, 1, 0);
    }
}
