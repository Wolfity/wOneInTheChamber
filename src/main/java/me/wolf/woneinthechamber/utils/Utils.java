package me.wolf.woneinthechamber.utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;


public final class Utils {

    public static String colorize(final String input) {
        return input == null ? "Null value" : ChatColor.translateAlternateColorCodes('&', input);
    }

    public static String[] colorize(String... messages) {
        String[] colorized = new String[messages.length];
        for (int i = 0; i < messages.length; i++) {
            colorized[i] = ChatColor.translateAlternateColorCodes('&', messages[i]);
        }
        return colorized;
    }

    public static boolean isInArena(final Player player, final Location loc1, final Location loc2) {
        final double[] dim = new double[2];

        dim[0] = loc1.getX();
        dim[1] = loc2.getX();
        Arrays.sort(dim);
        if (player.getLocation().getX() > dim[1] || player.getLocation().getX() < dim[0])
            return false;

        dim[0] = loc1.getZ();
        dim[1] = loc2.getZ();
        Arrays.sort(dim);
        return !(player.getLocation().getZ() > dim[1]) && !(player.getLocation().getZ() < dim[0]);
    }

    private Utils() {
    }

}
