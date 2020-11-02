package com.sammurphy.sleepPercentage.lookups;

public enum MessageComponents
{
    MESSAGE("Message"),
    COLORS("Color"),
    ADMIN("Admin"),
    ERROR("Error");

    private final String functionName;

    MessageComponents(String functionName) {
        this.functionName = functionName;
    }

    @Override
    public String toString() {
        return functionName;
    }
}
