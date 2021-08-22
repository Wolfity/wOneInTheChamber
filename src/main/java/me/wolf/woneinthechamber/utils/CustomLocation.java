package me.wolf.woneinthechamber.utils;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;


@AllArgsConstructor
public final class CustomLocation {

    private final String world;

    private final double x;
    private final double y;
    private final double z;

    private final float pitch;
    private final float yaw;

    public Location toBukkitLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z, pitch, yaw);
    }


    public String serialize() {
        return world + " " + x + " " + y + " " + z + " " + pitch + " " + yaw + " ";
    }

    public static CustomLocation fromBukkitLocation(final Location location) {
        return new CustomLocation(location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getPitch(),
                location.getYaw()
        );
    }

    public static CustomLocation deserialize(@NonNull final String location) {
        final String[] strings = location.split(" ");

        final String worldName = strings[0];

        final double x = Double.parseDouble(strings[1]);
        final double y = Double.parseDouble(strings[2]);
        final double z = Double.parseDouble(strings[3]);

        final float pitch = Float.parseFloat(strings[4]);
        final float yaw = Float.parseFloat(strings[5]);

        return new CustomLocation(worldName, x, y, z, pitch, yaw);
    }
}