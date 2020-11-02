package com.sammurphy.sleepPercentage.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import com.sammurphy.sleepPercentage.commandSystem.*;

import java.util.List;

public class CommandReloadConfig extends CommandTool {

    private enum DataSet
    {
        MESSAGES,
        CONFIG
    }

    protected CommandReloadConfig(JavaPlugin plugin) {
        super(plugin, "spreload",
                new ArgumentsProvider(CommandSenderType.BOTH, new ArgumentComponent(ArgumentType.ENUM, false).setCustomDataSet(DataSet.class).setCustomUsage("config/messages")));
    }

    @Override
    protected CommandSenderType getCommandSenderType() {
        return null;
    }

    @Override
    protected void onCommand(CommandSender sender, Command command, ArgumentMatchContainer args) {

    }

    @Override
    protected String getPermissionNode() {
        return null;
    }

    @Override
    protected List<String> getTabbedArguments(ArgumentComponent component, int tabbedIndexCount) {
        return null;
    }
}
