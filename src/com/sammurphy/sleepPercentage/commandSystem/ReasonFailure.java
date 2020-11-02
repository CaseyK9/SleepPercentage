package com.sammurphy.sleepPercentage.commandSystem;

import org.bukkit.ChatColor;

public enum ReasonFailure {
    //Overall Issues
    NOT_ENOUGH_ARGUMENTS("Not Enough Arguments."),
    SENDER_NEEDS_TO_BE_CONSOLE(ChatColor.RED + "You cannot issue this command."),
    SENDER_NEEDS_TO_BE_PLAYER(ChatColor.RED + "You need to be a" + ChatColor.LIGHT_PURPLE + " player" + ChatColor.RED + " to issue this command."),

    //System Issues
    VALUE_CHECK_OUTSIDE_RANGE("Value Check outside range."),
    UNKNOWN_ARGUMENT_TYPE("Unknown Argument Type."),

    //Argument Issues
    VALUE_TOO_HIGH("Value is too high."),
    VALUE_TOO_LOW("Value is too low."),
    INVALID_NUMBER(""),
    INVALID_DECIMAL("Needs to be a Decimal Number."),
    NO_ONLINE_PLAYER_FOUND("Player needs to be online."),
    NO_ONLINE_WORLD_FOUND("World needs to be loaded."),
    INVALID_BOOLEAN("Invalid."),
    INVALID_ENUM_CONSTANT("Invalid."),
    INVALID_COLOR("Color doesn't exist");

    private final String reason;

    ReasonFailure(String reason)
    {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return reason;
    }
}
