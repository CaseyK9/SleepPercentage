package com.sammurphy.sleepPercentage.commands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;
import com.sammurphy.sleepPercentage.config.GeneralConfig;
import com.sammurphy.sleepPercentage.MessageInterrupter;
import com.sammurphy.sleepPercentage.SleepPercentage;
import com.sammurphy.sleepPercentage.commandSystem.*;
import com.sammurphy.sleepPercentage.lookups.ConfigLookup;
import com.sammurphy.sleepPercentage.lookups.ConfigMessageLookup;
import com.sammurphy.sleepPercentage.lookups.PermissionLookup;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("deprecation")
public class CommandTimeOtherWorld extends CommandTool implements Listener {

    private enum DataSet
    {
        SET,
        ADD
    }

    //TODO: Arguments Stuff. None of it works yet
    public CommandTimeOtherWorld(JavaPlugin plugin) {
        super(plugin, "sptime",
                new ArgumentsProvider(CommandSenderType.PLAYER,
                        new ArgumentComponent(ArgumentType.WORLD_NAME, false)),
                new ArgumentsProvider(CommandSenderType.BOTH,
                        new ArgumentComponent(ArgumentType.WORLD_NAME, true)),
                new ArgumentsProvider(CommandSenderType.PLAYER,
                        new ArgumentComponent(ArgumentType.ENUM, true).setCustomDataSet(DataSet.class).setCustomUsage("set/add"),
                        new ArgumentComponent(ArgumentType.INTEGER, true).setRange(0, SleepPercentage.LAST_DAY_TICK)),
                new ArgumentsProvider(CommandSenderType.BOTH,
                        new ArgumentComponent(ArgumentType.WORLD_NAME, true),
                        new ArgumentComponent(ArgumentType.ENUM, true).setCustomDataSet(DataSet.class).setCustomUsage("set/add"),
                        new ArgumentComponent(ArgumentType.INTEGER, true).setRange(0, SleepPercentage.LAST_DAY_TICK)));

        if(GeneralConfig.self.getBoolean(ConfigLookup.overrloadTimeCommand()))
        {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    @Override
    protected CommandSenderType getCommandSenderType() {
        return CommandSenderType.BOTH;
    }

    @Override
    protected void onCommand(CommandSender sender, Command command, ArgumentMatchContainer args) {
        World world = null;
        DataSet option = null;
        int changeAmount = 0;

        switch(args.getArgumentCount())
        {
            case 0:
                world = ((Player) sender).getWorld();
                break;
            case 1:
                world = (World) args.getReturnedResults()[0];
                break;
            case 2:
            case 3:
                int currentArgCount = 0;
                for (ArgumentComponent component: args.getComponents()) {
                    switch(component.getArgumentType())
                    {
                        case ENUM:
                            option = (DataSet) args.getReturnedResults()[currentArgCount];
                            break;
                        case INTEGER:
                            changeAmount = (int)(float)args.getReturnedResults()[currentArgCount];
                            break;
                        case WORLD_NAME:
                            world = (World) args.getReturnedResults()[currentArgCount];
                            break;
                        default:
                            break;
                    }
                    currentArgCount++;
                }

                switch(Objects.requireNonNull(option))
                {
                    case SET:
                        world.setTime(changeAmount);
                        MessageInterrupter.process(ConfigMessageLookup.MessageSubNodes.ADJUST_TIME, plugin, sender, world.getTime(), world.getName(), sender.getName(), getNameForTimeRange(world.getTime()));
                        return;
                    case ADD:
                        world.setTime(world.getTime() + changeAmount);
                        MessageInterrupter.process(ConfigMessageLookup.MessageSubNodes.ADJUST_TIME, plugin, sender, world.getTime(), world.getName(), sender.getName(), getNameForTimeRange(world.getTime()));
                        break;
                }
                return;
        }

        MessageInterrupter.process(ConfigMessageLookup.MessageSubNodes.TIME_IN_WORLD, plugin, sender, world.getTime(), world.getName(), getNameForTimeRange(world.getTime()));
    }

    @Override
    protected String getPermissionNode() {
        return PermissionLookup.timeInOtherWorld();
    }

    @Override
    protected List<String> getTabbedArguments(ArgumentComponent component, int tabbedIndexCount) {
        return null;
    }

    @EventHandler
    public void commandPreProcess(PlayerCommandPreprocessEvent e)
    {
        String[] data;
        if((data = e.getMessage().split(" "))[0].equalsIgnoreCase("/time"))
        {
            data[0] = "/sptime";
            StringBuilder builder = new StringBuilder();
            for (String s : data) {
                builder.append(s + " ");
            }
            e.setMessage(builder.toString());
        }
    }

    //TODO: Need to fix how to inject this. May need to reflect open the object. Going to be fun.
    @EventHandler(priority = EventPriority.HIGHEST)
    public void tabPreProcess(PlayerChatTabCompleteEvent e)
    {
        plugin.getLogger().info("This is happening");
        String[] data;
        if((data = e.getChatMessage().split(" "))[0].equalsIgnoreCase("/time"))
        {
            data[0] = "/sptime";
            StringBuilder builder = new StringBuilder();
            for (String s : data) {
                builder.append(s + " ");
            }

            try {
                e.getClass().getField("message").set(e, builder.toString());
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }

            plugin.getLogger().info(e.getChatMessage());
        }
    }

    //TODO: Put this all onto config lookup
    private String getNameForTimeRange(long time)
    {
        if(time >= 22916)
        {
            return "Dawn";
        }
        else if(time == 18000)
        {
            return "Midnight";
        }
        else if(time >= 13800)
        {
            return "Night";
        }
        else if(time >= 11616)
        {
            return "Evening";
        }
        else if(time == 6000)
        {
            return "Midday";
        }
        else if(time >= 1000)
        {
            return "Morning";
        }
        else if(time >= 0)
        {
            return "Dawn";
        }
        else
        {
            return ChatColor.RED + "Outside of Time Scope";
        }
    }
}
