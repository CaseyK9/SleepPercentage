package com.sammurphy.sleepPercentage;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Timers {

    private static JavaPlugin plugin;

    private static final HashMap<Integer, Runnable> runnableMapping = new HashMap<>();

    public static void onDisable()
    {
        for (Runnable runnable : runnableMapping.values()) {
            runnable.run();
        }

        runnableMapping.clear();
    }

    public static void registerNewEvent(int scheduleID, Runnable runnable)
    {
        runnableMapping.put(scheduleID, runnable);
    }

    public static void initCleaningProcess(int interval)
    {
        Runnable task;
        int scheduleID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, task = () -> {
            for (Integer key : runnableMapping.keySet()) {
                if(!(Bukkit.getScheduler().isQueued(key) && Bukkit.getScheduler().isCurrentlyRunning(key)))
                {
                    runnableMapping.remove(key);
                }
            }
        }, interval, interval);

        registerNewEvent(scheduleID, task);
    }

    public static void setPlugin(JavaPlugin plugin)
    {
        Timers.plugin = plugin;
    }

    public static int getTotalHandles()
    {
        return runnableMapping.size();
    }
}