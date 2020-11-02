package com.sammurphy.sleepPercentage.commandSystem;

import org.bukkit.ChatColor;

public class ErrorColor {

    private enum ErrorColors
    {
        AQUA(ChatColor.AQUA),
        GREEN(ChatColor.GREEN),
        YELLOW(ChatColor.YELLOW),
        DARK_BLUE(ChatColor.DARK_BLUE),
        DARK_GREEN(ChatColor.DARK_GREEN),
        DARK_AQUA(ChatColor.DARK_AQUA);

        private final ChatColor color;

        ErrorColors(ChatColor color) {
            this.color = color;
        }

        public ChatColor getColor() {
            return color;
        }
    }

    public static ChatColor getErrorColor(int index)
    {
        return ErrorColors.values()[index % ErrorColors.values().length].getColor();
    }
}
