package com.sammurphy.sleepPercentage.commands;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import com.sammurphy.sleepPercentage.commandSystem.CommandTool;
import com.sammurphy.sleepPercentage.events.DisableEvent;
import com.sammurphy.sleepPercentage.events.SleepPercentageOnDisable;

import java.util.HashMap;

public class CommandRegister implements Listener, DisableEvent {

    private final HashMap<String, CommandTool> commands = new HashMap<>();

    public CommandRegister(JavaPlugin plugin) {
//        registerCommand(new CommandSpToggle(plugin));
        registerCommand(new CommandSpSetPlugin(plugin));
        registerCommand(new CommandSpSet(plugin));
        registerCommand(new CommandSpDayTick(plugin));
        registerCommand(new CommandSpEnableWorld(plugin));
        registerCommand(new CommandTimeOtherWorld(plugin));

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private void registerCommand(CommandTool commandTool)
    {
        commands.put(commandTool.getCommandName(), commandTool);
    }

    @Override
    public void onDisable(SleepPercentageOnDisable e) {
        commands.clear();
    }
}
