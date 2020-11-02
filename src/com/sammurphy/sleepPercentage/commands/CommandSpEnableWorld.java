package com.sammurphy.sleepPercentage.commands;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.sammurphy.sleepPercentage.config.GeneralConfig;
import com.sammurphy.sleepPercentage.MessageInterrupter;
import com.sammurphy.sleepPercentage.commandSystem.*;
import com.sammurphy.sleepPercentage.lookups.ConfigLookup;
import com.sammurphy.sleepPercentage.lookups.ConfigMessageLookup;
import com.sammurphy.sleepPercentage.lookups.PermissionLookup;

import java.util.List;

public class CommandSpEnableWorld extends CommandTool {

    public CommandSpEnableWorld(JavaPlugin plugin) {
        super(plugin, "spenable",
                new ArgumentsProvider(CommandSenderType.PLAYER,
                        new ArgumentComponent(ArgumentType.WORLD_NAME, false),
                        new ArgumentComponent(ArgumentType.BOOLEAN, true)
                ),
                new ArgumentsProvider(CommandSenderType.PLAYER,
                        new ArgumentComponent(ArgumentType.WORLD_NAME, false)),
                new ArgumentsProvider(CommandSenderType.CONSOLE,
                        new ArgumentComponent(ArgumentType.WORLD_NAME, true)
                ),
                new ArgumentsProvider(CommandSenderType.CONSOLE,
                        new ArgumentComponent(ArgumentType.WORLD_NAME, true),
                        new ArgumentComponent(ArgumentType.BOOLEAN, true)));
    }

    @Override
    protected CommandSenderType getCommandSenderType() {
        return CommandSenderType.BOTH;
    }

    @Override
    protected void onCommand(CommandSender sender, Command command, ArgumentMatchContainer args) {
        Object[] data = args.getReturnedResults();

        String worldName = "Unknown World";
        boolean result = false;

        for(int i = 0; i < data.length; i++)
        {
            switch(args.getComponents()[i].getArgumentType())
            {
                case WORLD_NAME:
                    if(data[i] instanceof OptionalArgument)
                    {
                        worldName = ((Player)sender).getWorld().getName();
                    }
                    else
                    {
                        worldName = ((World)data[i]).getName();
                    }
                    break;
                case BOOLEAN:
                    result = (boolean) data[i];
                    break;
			case COLOR:
				break;
			case ENUM:
				break;
			case FLOAT:
				break;
			case INTEGER:
				break;
			case PLAYER_NAME:
				break;
			case STRING:
				break;
			default:
				break;
            }
        }

        if(data.length <= 1)
        {
            MessageInterrupter.process(ConfigMessageLookup.MessageSubNodes.WORLD_DISABLE_STATUS, plugin, sender, configGet(worldName), worldName);
        }
        else
        {
            configSet(worldName, result);
            MessageInterrupter.process(ConfigMessageLookup.MessageSubNodes.WORLD_DISABLE, plugin, sender, result, worldName, sender.getName());
        }
    }

    @Override
    protected String getPermissionNode() {
        return PermissionLookup.disable();
    }

    @Override
    protected List<String> getTabbedArguments(ArgumentComponent component, int tabbedIndexCount) {
        return null;
    }

    private boolean configGet(String worldName)
    {
        return !GeneralConfig.self.getBoolean(ConfigLookup.worldDisable(worldName));
    }

    private void configSet(String worldName, boolean value)
    {
        GeneralConfig.self.set(ConfigLookup.worldDisable(worldName), !value);
        plugin.saveConfig();
    }
}
