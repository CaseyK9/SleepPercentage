package com.sammurphy.sleepPercentage.commandSystem;

import org.bukkit.ChatColor;

public class ArgumentError
{
    private final ArgumentType argumentType;
    private final Object argumentValue;

    private final ReasonFailure reason;

    public ArgumentError(ArgumentType argumentType, Object argumentValue, ReasonFailure reason)
    {
        this.argumentType = argumentType;
        this.argumentValue = argumentValue;
        this.reason = reason;
    }

    public ReasonFailure getReason()
    {
        return reason;
    }

    //TODO: Integers output as floats
    public String toString(int index)
    {
        return ErrorColor.getErrorColor(index) + (argumentValue == null ? "Blank" : argumentValue.toString()) + ChatColor.WHITE
                + ": " + reason.toString() + " Needs to be an " + ChatColor.GOLD + argumentType.toString();
    }

    @Override
    public String toString() {
        return "ArgumentError{" +
                "argumentType=" + argumentType +
                ", argumentValue=" + argumentValue +
                ", reason=" + reason +
                '}';
    }
}