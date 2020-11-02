package com.sammurphy.sleepPercentage.eventManagement;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;
import com.sammurphy.sleepPercentage.config.GeneralConfig;
import com.sammurphy.sleepPercentage.MessageInterrupter;
import com.sammurphy.sleepPercentage.SleepPercentage;
import com.sammurphy.sleepPercentage.Timers;
import com.sammurphy.sleepPercentage.events.RightClickBedEvent;
import com.sammurphy.sleepPercentage.lookups.ConfigLookup;
import com.sammurphy.sleepPercentage.lookups.ConfigMessageLookup;
import com.sammurphy.sleepPercentage.lookups.ConfigPermissionLookup;
import com.sammurphy.sleepPercentage.lookups.PermissionLookup;

import java.util.Collection;
import java.util.UUID;

public class PlayerEvents implements Listener, EventConfig {

    private final SleepPercentage sleepPercentage;

    public PlayerEvents(SleepPercentage sleepPercentage) {
        this.sleepPercentage = sleepPercentage;

        sleepPercentage.getServer().getPluginManager().registerEvents(this, sleepPercentage);
    }

    @EventHandler
    public void onServerJoin(PlayerJoinEvent e)
    {
        if(SleepPercentage.BUILD_TYPE == SleepPercentage.BuildType.DEVELOPMENT || SleepPercentage.BUILD_TYPE == SleepPercentage.BuildType.UNSTABLE)
            e.getPlayer().sendMessage(ChatColor.GRAY + "This server is running a " + ChatColor.RED + SleepPercentage.BUILD_TYPE + " - " + SleepPercentage.TARGET_VERSION_NUMBER + " BUILD" + ChatColor.GRAY + " of " + ChatColor.LIGHT_PURPLE + "Sleep Percentage" + ChatColor.GRAY + ". Please report issues to " + ChatColor.GOLD + "http://bit.ly/SPIssues");
    }

    @EventHandler
    public void onServerLeave(PlayerQuitEvent e) {
        onBedLeave(e.getPlayer().getUniqueId(), e.getPlayer().getWorld());
    }

    @EventHandler
    public void onServerLeave(PlayerKickEvent e) {
        onBedLeave(e.getPlayer().getUniqueId(), e.getPlayer().getWorld());
    }

    public void onBedLeave(UUID uuid, World world) {
        if(world != null)
            if(sleepPercentage.getPlayersSleeping().containsKey(world.getUID()))
                if (sleepPercentage.getPlayersSleeping().get(world.getUID()).contains(uuid)) {
                    sleepPercentage.getPlayersSleeping().get(world.getUID()).remove(uuid);
                    long time = world.getTime();
                    if (((time >= SleepPercentage.START_BED_TIME && time <= SleepPercentage.END_BED_TIME) || world.isThundering()) && GeneralConfig.self.getBoolean(ConfigLookup.enabled())) {
                        MessageInterrupter.process(ConfigMessageLookup.MessageSubNodes.NOT_SLEEPING, sleepPercentage, world,
                                Bukkit.getPlayer(uuid).getDisplayName(), world.getName());

                        sleepPercentage.broadcastPercentageSleepingMessage(world, sleepPercentage.getTotalPlayersForWorld(world));
                    }

                    //TODO: Add check incase sleeping is active. Needs to be silent check.
                }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPreBedEnterPermissionCheck(RightClickBedEvent e)
    {
        if(!e.getPlayer().hasPermission(PermissionLookup.sleepInWorldSpecific(e.getWorld().getName())))
        {
            e.setCancelled(true);
            MessageInterrupter.process(ConfigPermissionLookup.PermissionSubNodes.SLEEPING_IN_WORLD_WILDCARD, sleepPercentage, e.getPlayer(), e.getWorld().getName());
        }
    }

    @EventHandler
    public void onPreBedEnterMobCheck(RightClickBedEvent e)
    {
        String worldName = e.getPlayer().getWorld().getName();

        if(!GeneralConfig.self.getBoolean(ConfigLookup.worldMobNearbyEnabled(worldName))) { return; }

        if(GeneralConfig.self.getBoolean(ConfigLookup.worldMobNearbyIgnorePermission(worldName)) || e.getPlayer().hasPermission(
                PermissionLookup.sleepNearMobWorldSpecific(worldName))) {
            long time = e.getPlayer().getWorld().getTime();
            if (((time >= SleepPercentage.START_BED_TIME && time <= SleepPercentage.END_BED_TIME) || e.getPlayer().getWorld().isThundering()))
            {
                Location location = e.getPlayer().getLocation();
                Collection<Entity> entities = location.getWorld().getNearbyEntities(location, 16, 10, 16);

                entities.forEach(entity -> {
                    if(entity instanceof LivingEntity && !(entity instanceof Player))
                    {
                        if(GeneralConfig.self.getBoolean(ConfigLookup.worldMobNearbyNameTag(worldName)))
                        {
                            if(entity.getCustomName() == null) { return; }
                        }

                        Runnable task;
                        int scheduledID = Bukkit.getScheduler().scheduleSyncDelayedTask(sleepPercentage, task = () -> {
                            entity.setVelocity(new Vector());
                            entity.teleport(entity.getLocation().subtract(new Vector(0, 256, 0)));
                            ((LivingEntity)entity).setAI(true);
                        }, GeneralConfig.self.getInt(ConfigLookup.worldMobNearbyDelay(worldName)));

                        if(scheduledID != -1)
                        {
                            Timers.registerNewEvent(scheduledID, task);
                        }
                        else
                        {
                            e.setCancelled(true);
                            return;
                        }

                        entity.teleport(entity.getLocation().add(new Vector(0, 256, 0)));
                        ((LivingEntity) entity).setAI(false);
                    }
                });
            }
        }
    }

    @EventHandler
    public void onRightClickAction(PlayerInteractEvent e)
    {
        if(!GeneralConfig.self.getBoolean(ConfigLookup.enabled())) { return; }

        if(GeneralConfig.self.getBoolean(ConfigLookup.worldDisable(e.getPlayer().getWorld().getName()))) { return; }

        if(e.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if(isBed(e.getClickedBlock().getType()))
            {
                sleepPercentage.getServer().getPluginManager().callEvent(new RightClickBedEvent(e.getPlayer()));
            }
        }
    }

    private boolean isBed(Material material)
    {
        switch(material)
        {
            case BLACK_BED:
            case BLUE_BED:
            case BROWN_BED:
            case CYAN_BED:
            case GRAY_BED:
            case GREEN_BED:
            case LIGHT_BLUE_BED:
            case LIGHT_GRAY_BED:
            case LIME_BED:
            case MAGENTA_BED:
            case ORANGE_BED:
            case PINK_BED:
            case PURPLE_BED:
            case RED_BED:
            case WHITE_BED:
            case YELLOW_BED:
                return true;
            default:
                return false;
        }
    }

    @Override
    public FileConfiguration getConfig() {
        return GeneralConfig.self;
    }
}
