package dev.tswanson.lockout;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Random;

public class SpawnHelper {
    private static final double SPAWN_RADIUS = 30;

    private static Random random = new Random();

    public static Location getRandomSpawnpoint(World world) {
        double theta = (2 * Math.PI) + (2 * Math.PI) * random.nextDouble();
        double x = SPAWN_RADIUS * Math.cos(theta);
        double z = SPAWN_RADIUS * Math.sin(theta);
        return world.getHighestBlockAt(new Location(world, x, 0, z)).getLocation().add(0, 1, 0);
    }
}
