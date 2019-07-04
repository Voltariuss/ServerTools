package fr.dornacraft.servertools.model.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class GlobalManager {

    public static Location spawnLocation;

    public static Location getSpawnLocation() {
        return spawnLocation;
    }

    public static void setSpawnLocation(JavaPlugin javaPlugin) {
        FileConfiguration config = javaPlugin.getConfig();
        World world = Bukkit.getWorld(config.getString("spawn_location.world"));
        double x = config.getDouble("spawn_location.x");
        double y = config.getDouble("spawn_location.y");
        double z = config.getDouble("spawn_location.z");
        float pitch = (float) config.getDouble("spawn_location.pitch");
        float yaw = (float) config.getDouble("spawn_location.yaw");
        spawnLocation = new Location(world, x, y, z, pitch, yaw);
    }
}