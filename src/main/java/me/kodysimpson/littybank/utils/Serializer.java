package me.kodysimpson.littybank.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Serializer {

    public static String serializeLocation(Location location) {
        String worldName = location.getWorld().getName();
        String x = String.valueOf(location.getX());
        String y = String.valueOf(location.getY());
        String z = String.valueOf(location.getZ());

        return worldName + "A" + x + "B" + y + "C" + z;
    }

    public static Location deserializeLocation(String location) {
        World world = Bukkit.getWorld(location.substring(0, location.indexOf("A")));
        double x = Double.parseDouble(location.substring(location.indexOf("A")+1, location.indexOf("B")));
        double y = Double.parseDouble(location.substring(location.indexOf("B")+1, location.indexOf("C")));
        double z = Double.parseDouble(location.substring(location.indexOf("C")+1 ));

        return new Location(world, x, y, z);
    }

}
