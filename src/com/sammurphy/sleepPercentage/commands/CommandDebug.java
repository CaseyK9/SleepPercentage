package com.sammurphy.sleepPercentage.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import com.sammurphy.sleepPercentage.SleepPercentage;
import com.sammurphy.sleepPercentage.Timers;
import com.sammurphy.sleepPercentage.commandSystem.*;

import java.util.List;

public class CommandDebug extends CommandTool {
    protected CommandDebug(JavaPlugin plugin) {
        super(plugin, "spdebug", new ArgumentsProvider(CommandSenderType.BOTH));
    }

    @Override
    protected CommandSenderType getCommandSenderType() {
        return CommandSenderType.BOTH;
    }

    @Override
    protected void onCommand(CommandSender sender, Command command, ArgumentMatchContainer args) {
        sender.sendMessage(new String[]{
                ChatColor.GOLD + "Plugin: " + ChatColor.LIGHT_PURPLE + "Sleep Percentage",
                ChatColor.GOLD + "Plugin Version: " + ChatColor.LIGHT_PURPLE + SleepPercentage.VERSION_NUMBER,
                ChatColor.GOLD + "Plugin Build: " + ChatColor.LIGHT_PURPLE + SleepPercentage.BUILD_TYPE,
                ChatColor.GOLD + "Targeted Game Version: " + ChatColor.LIGHT_PURPLE + SleepPercentage.TARGET_VERSION_NUMBER,
                ChatColor.GOLD + "Game Version: " + ChatColor.LIGHT_PURPLE + plugin.getServer().getVersion(),
                "",
                ChatColor.GOLD + "Time Handler Total: " + ChatColor.LIGHT_PURPLE + Timers.getTotalHandles(),
                ChatColor.GOLD + "Command Ingest Manager: " + ChatColor.LIGHT_PURPLE + "Operational",
        });
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
