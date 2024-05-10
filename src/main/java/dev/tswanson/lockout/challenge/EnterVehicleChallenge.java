package dev.tswanson.lockout.challenge;

import dev.tswanson.lockout.Challenge;
import dev.tswanson.lockout.gui.Icon;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class EnterVehicleChallenge implements Challenge {

    private final Icon icon;
    private final EntityType vehicleType;
    private final String name;

    public EnterVehicleChallenge(Icon icon, String name, EntityType vehicleType) {
        this.icon = icon;
        this.name = name;
        this.vehicleType = vehicleType;
    }

    @Override
    public Icon getIcon() {
        return this.icon;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player p) {
            if (event.getVehicle().getType() == this.vehicleType) markCompleted(p);
        }
    }
}
