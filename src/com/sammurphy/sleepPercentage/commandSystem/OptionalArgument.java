package com.sammurphy.sleepPercentage.commandSystem;

public class OptionalArgument
{
    private final ArgumentError error;

    public OptionalArgument(ArgumentError error)
    {
        this.error = error;
    }

    public ArgumentError getError()
    {
        return error;
    }

    @Override
    public String toString() {
        return "OptionalArgument{" +
                "error=" + error +
                '}';
    }
}
