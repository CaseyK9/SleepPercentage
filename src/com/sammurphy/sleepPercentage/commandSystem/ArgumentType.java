package com.sammurphy.sleepPercentage.commandSystem;

import org.bukkit.ChatColor;

public enum ArgumentType {
    STRING("Word/Phase"),
    FLOAT("Decimal Number"),
    INTEGER("Number"),
    PLAYER_NAME("Player Name"),
    WORLD_NAME("World Name"),
    BOOLEAN("true/false"),
    ENUM("Enum"),
    COLOR(generateEnumCommaList(ChatColor.class));

    private final String friendlyName;

    ArgumentType(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return friendlyName;
    }

    private static String generateEnumCommaList(Class<?> classRef)
    {
        StringBuilder builder = new StringBuilder();
        for (Object o : classRef.getEnumConstants()) {
            if(builder.length() == 0)
            {
                builder.append(o.toString());
            }
            else
            {
                builder.append(", " + o.toString());
            }
        }

        return builder.toString();
    }
}
