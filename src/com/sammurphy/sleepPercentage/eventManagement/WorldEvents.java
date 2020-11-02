package com.sammurphy.sleepPercentage.eventManagement;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.permissions.Permission;
import com.sammurphy.sleepPercentage.config.GeneralConfig;
import com.sammurphy.sleepPercentage.lookups.ConfigLookup;
import com.sammurphy.sleepPercentage.lookups.PermissionLookup;
import com.sammurphy.sleepPercentage.SleepPercentage;

import java.util.Arrays;

public class WorldEvents implements Listener, EventConfig {

    private final SleepPercentage sleepPercentage;

    public WorldEvents(SleepPercentage sleepPercentage) {
        this.sleepPercentage = sleepPercentage;

        sleepPercentage.getServer().getPluginManager().registerEvents(this, sleepPercentage);
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e)
    {
        processWorld(e.getWorld());
    }

    public void processWorld(World world)
    {
        String worldName = world.getName();

        if(!GeneralConfig.self.isSet(ConfigLookup.worldDisable(worldName)))
            GeneralConfig.self.set(ConfigLookup.worldDisable(worldName), false);

        if(!GeneralConfig.self.isSet(ConfigLookup.worldPercentage(worldName)))
            GeneralConfig.self.set(ConfigLookup.worldPercentage(worldName), 50);

        if(!GeneralConfig.self.isSet(ConfigLookup.worldTimeSet(worldName)))
            GeneralConfig.self.set(ConfigLookup.worldTimeSet(worldName), 1000);

        if(!GeneralConfig.self.isSet(ConfigLookup.worldWeatherTick(worldName)))
            GeneralConfig.self.set(ConfigLookup.worldWeatherTick(worldName), 54000);

        if(!GeneralConfig.self.isSet(ConfigLookup.worldChangeWeather(worldName)))
            GeneralConfig.self.set(ConfigLookup.worldChangeWeather(worldName), true);

        if(!GeneralConfig.self.isSet(ConfigLookup.worldMobNearbyEnabled(worldName)))
            GeneralConfig.self.set(ConfigLookup.worldMobNearbyEnabled(worldName), false);

        if(!GeneralConfig.self.isSet(ConfigLookup.worldMobNearbyDelay(worldName)))
            GeneralConfig.self.set(ConfigLookup.worldMobNearbyDelay(worldName), 10);

        if(!GeneralConfig.self.isSet(ConfigLookup.worldMobNearbyNameTag(worldName)))
            GeneralConfig.self.set(ConfigLookup.worldMobNearbyNameTag(worldName), true);

        if(!GeneralConfig.self.isSet(ConfigLookup.worldMobNearbyIgnorePermission(worldName)))
            GeneralConfig.self.set(ConfigLookup.worldMobNearbyIgnorePermission(worldName), false);

        if(!GeneralConfig.self.isSet(ConfigLookup.worldSleepingPermission(worldName)))
            GeneralConfig.self.set(ConfigLookup.worldSleepingPermission(worldName), false);

        if(!GeneralConfig.self.isSet(ConfigLookup.worldExcludeGameModes(worldName)))
            GeneralConfig.self.set(ConfigLookup.worldExcludeGameModes(worldName), Arrays.asList("CREATIVE", "SPECTATOR"));

        //Dynamic Permission Adding

        //Add Mob Nearby World
        Permission newPermission = new Permission(PermissionLookup.sleepNearMobWorldSpecific(world.getName()));

        newPermission.addParent(PermissionLookup.sleepNearMobWorldWildCard(), true);
        Bukkit.getPluginManager().addPermission(newPermission);

        //Add Sleep in World
        newPermission = new Permission(PermissionLookup.sleepInWorldSpecific(world.getName()));
        newPermission.addParent(PermissionLookup.sleepInWorldWildCard(), true);
        Bukkit.getPluginManager().addPermission(newPermission);

        //Add Exclude from Sleep Checks
        newPermission = new Permission(PermissionLookup.excludeSleepingChecksSpecific(world.getName()));
        newPermission.addParent(PermissionLookup.excludeSleepingChecksWildCard(), true);
        Bukkit.getPluginManager().addPermission(newPermission);

        sleepPercentage.saveConfig();
    }

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent e)
    {
        sleepPercentage.getPlayersSleeping().remove(e.getWorld().getUID());
    }

    @Override
    public FileConfiguration getConfig() {
        return GeneralConfig.self;
    }
}
