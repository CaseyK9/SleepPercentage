package com.sammurphy.sleepPercentage.eventManagement;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import com.sammurphy.sleepPercentage.config.GeneralConfig;
import com.sammurphy.sleepPercentage.lookups.ConfigLookup;
import com.sammurphy.sleepPercentage.MessageInterrupter;
import com.sammurphy.sleepPercentage.SleepPercentage;
import com.sammurphy.sleepPercentage.lookups.ConfigMessageLookup;

import java.util.ArrayList;
import java.util.UUID;

public class BedEvents implements Listener, EventConfig {

    private final SleepPercentage sleepPercentage;
    private final PlayerEvents playerEvents;

    public BedEvents(SleepPercentage sleepPercentage, PlayerEvents playerEvents) {
        this.sleepPercentage = sleepPercentage;
        this.playerEvents = playerEvents;
        sleepPercentage.getServer().getPluginManager().registerEvents(this, sleepPercentage);
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event) {
        if(!GeneralConfig.self.getBoolean(ConfigLookup.enabled()))
            return;

        UUID worldUID;
        if(!sleepPercentage.getPlayersSleeping().containsKey(worldUID = event.getPlayer().getWorld().getUID()))
            sleepPercentage.getPlayersSleeping().put(worldUID, new ArrayList<>());

        if(!GeneralConfig.self.getBoolean(ConfigLookup.worldDisable(event.getPlayer().getWorld().getName())))
        {
            sleepPercentage.getPlayersSleeping().get(worldUID).add(event.getPlayer().getUniqueId());
            MessageInterrupter.process(ConfigMessageLookup.MessageSubNodes.SLEEPING, sleepPercentage, event.getPlayer().getWorld(),
                    event.getPlayer().getDisplayName(), event.getPlayer().getWorld().getName());
            sleepPercentage.testForSleepPercentage(worldUID, event.getPlayer().getWorld());
        }
    }

    @EventHandler
    public void onBedLeave(PlayerBedLeaveEvent event) {
        playerEvents.onBedLeave(event.getPlayer().getUniqueId(), event.getPlayer().getWorld());
    }

    @Override
    public FileConfiguration getConfig()
    {
        return GeneralConfig.self;
    }
}
