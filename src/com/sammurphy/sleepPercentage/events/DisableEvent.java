package com.sammurphy.sleepPercentage.events;

import org.bukkit.event.EventHandler;

public interface DisableEvent {

    @EventHandler
    void onDisable(SleepPercentageOnDisable e);

}
