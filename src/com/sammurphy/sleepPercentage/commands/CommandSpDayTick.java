package com.sammurphy.sleepPercentage.commands;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.sammurphy.sleepPercentage.config.GeneralConfig;
import com.sammurphy.sleepPercentage.lookups.ConfigLookup;
import com.sammurphy.sleepPercentage.MessageInterrupter;
import com.sammurphy.sleepPercentage.lookups.ConfigMessageLookup;
import com.sammurphy.sleepPercentage.lookups.PermissionLookup;
import com.sammurphy.sleepPercentage.SleepPercentage;
import com.sammurphy.sleepPercentage.commandSystem.*;

import java.util.List;

/**
 * Created by Sam Murphy on 25/05/2017.
 */
public class CommandSpDayTick extends CommandTool {
	public CommandSpDayTick(JavaPlugin plugin) {
		super(plugin, "spdaytick",
				new ArgumentsProvider(CommandSenderType.PLAYER,
                        new ArgumentComponent(ArgumentType.WORLD_NAME, false)),
				new ArgumentsProvider(CommandSenderType.PLAYER,
						new ArgumentComponent(ArgumentType.WORLD_NAME, false),
						new ArgumentComponent(ArgumentType.INTEGER, true).setRange(0, SleepPercentage.LAST_DAY_TICK)
				),
				new ArgumentsProvider(CommandSenderType.CONSOLE,
                        new ArgumentComponent(ArgumentType.WORLD_NAME, true)),
				new ArgumentsProvider(CommandSenderType.CONSOLE,
						new ArgumentComponent(ArgumentType.WORLD_NAME, true),
						new ArgumentComponent(ArgumentType.INTEGER, true).setRange(0, SleepPercentage.LAST_DAY_TICK)));
	}

	@Override
	protected CommandSenderType getCommandSenderType() {
		return CommandSenderType.BOTH;
	}

	@Override
	protected void onCommand(CommandSender sender, Command command, ArgumentMatchContainer args) {
        Object[] data = args.getReturnedResults();

        String worldName = "Unknown World";
        int value = -1;

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
                case INTEGER:
                    value =  Math.round((float) data[i]);
                    break;
			case BOOLEAN:
				break;
			case COLOR:
				break;
			case ENUM:
				break;
			case FLOAT:
				break;
			case PLAYER_NAME:
				break;
			case STRING:
				break;
			default:
				break;
            }
        }

        if(value == -1)
        {
            MessageInterrupter.process(ConfigMessageLookup.MessageSubNodes.DAY_TICK_VALUE, plugin, sender, worldName, configGet(worldName));
        }
        else
        {
            configSet(worldName, value);
            MessageInterrupter.process(ConfigMessageLookup.MessageSubNodes.DAY_TICK, plugin, sender, value, worldName, sender.getName());
        }
	}

    @Override
    protected String getPermissionNode() {
        return PermissionLookup.dayTick();
    }

	@Override
	protected List<String> getTabbedArguments(ArgumentComponent component, int tabbedIndexCount) {
		return null;
	}

	private int configGet(String worldName)
    {
        return GeneralConfig.self.getInt(ConfigLookup.worldTimeSet(worldName));
    }

	private void configSet(String worldName, int number)
    {
        GeneralConfig.self.set(ConfigLookup.worldTimeSet(worldName), number);
        GeneralConfig.self.save();
    }
}
