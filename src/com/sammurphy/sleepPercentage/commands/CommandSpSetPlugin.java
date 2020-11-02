package com.sammurphy.sleepPercentage.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import com.sammurphy.sleepPercentage.config.GeneralConfig;
import com.sammurphy.sleepPercentage.MessageInterrupter;
import com.sammurphy.sleepPercentage.commandSystem.*;
import com.sammurphy.sleepPercentage.lookups.ConfigLookup;
import com.sammurphy.sleepPercentage.lookups.ConfigMessageLookup;
import com.sammurphy.sleepPercentage.lookups.PermissionLookup;

import java.util.List;

public class CommandSpSetPlugin extends CommandTool {
    protected CommandSpSetPlugin(JavaPlugin plugin) {
        super(plugin, "sp",
                new ArgumentsProvider(CommandSenderType.BOTH,
                        new ArgumentComponent(ArgumentType.BOOLEAN, false)));
    }

    @Override
    protected CommandSenderType getCommandSenderType() {
        return CommandSenderType.BOTH;
    }

    @Override
    protected void onCommand(CommandSender sender, Command command, ArgumentMatchContainer args)
    {
        if(!(args.getReturnedResults()[0] instanceof OptionalArgument))
        {
            GeneralConfig.self.set(ConfigLookup.enabled(), args.getReturnedResults()[0]);
            GeneralConfig.self.save();
            MessageInterrupter.process(ConfigMessageLookup.MessageSubNodes.PLUGIN_CHANGE_STATE, plugin, sender, GeneralConfig.self.getBoolean(ConfigLookup.enabled()), sender.getName());
        }
        else
        {
            MessageInterrupter.process(ConfigMessageLookup.MessageSubNodes.PLUGIN_STATE, plugin, sender, GeneralConfig.self.get(ConfigLookup.enabled()));
        }
    }

    @Override
    protected String getPermissionNode() {
        return PermissionLookup.toggle();
    }

    @Override
    protected List<String> getTabbedArguments(ArgumentComponent component, int tabbedIndexCount) {
        return null;
    }
}
