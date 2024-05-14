package dev.tswanson.lockout.challenge;

import dev.tswanson.lockout.Challenge;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public record EnterVehicleChallenge(ChallengeMetadata metadata, EntityType vehicleType) implements Challenge {

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player p) {
            if (event.getVehicle().getType() == this.vehicleType) markCompleted(p);
        }
    }
}
