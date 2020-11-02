package com.sammurphy.sleepPercentage.commandSystem;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandPatterns {

    public static class WorldNameInt
    {
        public final String worldName;
        public final int number;

        WorldNameInt(String worldName, int number) {
            this.worldName = worldName;
            this.number = number;
        }
    }

    public static WorldNameInt patternConvertArgumentsToWorldNameInt(Object[] data, CommandSender sender)
    {
        String worldName = "Unknown World";
        int number = 0;

        switch(data.length)
        {
            case 1:
                worldName = ((Player)sender).getWorld().getName();
                number = Math.round((Float) data[0]);
                break;
            case 2:
                number = Math.round((Float) data[1]);
                if(!(data[0] instanceof OptionalArgument))
                {
                    worldName = ((World)data[0]).getName();
                }
                else
                {
                    worldName = ((Player)sender).getWorld().getName();
                }
                break;
        }

        return new WorldNameInt(worldName, number);
    }

}
