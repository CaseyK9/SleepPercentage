package com.sammurphy.sleepPercentage.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SleepPercentageOnDisable extends Event {

    private static final HandlerList handlers = new HandlerList();

    public SleepPercentageOnDisable() { }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
