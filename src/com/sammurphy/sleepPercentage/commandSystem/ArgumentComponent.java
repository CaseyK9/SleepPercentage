package com.sammurphy.sleepPercentage.commandSystem;

import org.bukkit.Bukkit;
import java.util.ArrayList;
import java.util.List;

public final class ArgumentComponent
{
    private final boolean required;

    private float minRange = Float.MIN_VALUE;
    private float maxRange = Float.MAX_VALUE;

    private Class<?> customDataSet;

    private final ArgumentType argumentType;
    private final Object internalObjectClassification;

    private String customUsageName;

    public ArgumentComponent(ArgumentType argumentType, boolean required)
    {
        this(argumentType, required, null);
    }

    public ArgumentComponent(ArgumentType argumentType, boolean required, Object internalObjectClassification)
    {
        this.required = required;
        this.argumentType = argumentType;
        this.internalObjectClassification = internalObjectClassification;
    }

    public Object passArgumentChecks(String argumentPassIn)
    {
        switch (argumentType)
        {
            case FLOAT:
                try
                {
                    float value = Float.valueOf(argumentPassIn);
                    return numberResult(value);

                }
                catch (NumberFormatException e)
                {
                    return new ArgumentError(argumentType, argumentPassIn, ReasonFailure.INVALID_DECIMAL);
                }
            case INTEGER:
                try
                {
                    int value = Integer.valueOf(argumentPassIn);
                    return numberResult(value);
                }
                catch (NumberFormatException e)
                {
                    return new ArgumentError(argumentType, argumentPassIn, ReasonFailure.INVALID_NUMBER);
                }
            case PLAYER_NAME:
                return Bukkit.getOnlinePlayers().stream().filter(player -> player.getName().equals(argumentPassIn))
                        .findFirst().<Object>map(player -> player).orElse(new ArgumentError(argumentType, argumentPassIn, ReasonFailure.NO_ONLINE_PLAYER_FOUND));
            case WORLD_NAME:
                return Bukkit.getWorlds().stream().filter(world -> world.getName().equals(argumentPassIn))
                        .findFirst().<Object>map(world -> world).orElse(new ArgumentError(argumentType, argumentPassIn, ReasonFailure.NO_ONLINE_WORLD_FOUND));
            case STRING:
                return argumentPassIn;
            case BOOLEAN:
                if(argumentPassIn.equalsIgnoreCase("true") || argumentPassIn.equalsIgnoreCase("false"))
                {
                    return Boolean.valueOf(argumentPassIn);
                }
                else
                {
                    return new ArgumentError(ArgumentType.BOOLEAN, argumentPassIn, ReasonFailure.INVALID_BOOLEAN);
                }
            case ENUM:
                for (Object o : customDataSet.getEnumConstants()) {
                    if(o.toString().equalsIgnoreCase(argumentPassIn))
                    {
                        return o;
                    }
                }
                return new ArgumentError(ArgumentType.ENUM, argumentPassIn, ReasonFailure.INVALID_ENUM_CONSTANT);
            case COLOR:
                try {
                }
                catch (IllegalArgumentException e) {
                    return new ArgumentError(ArgumentType.COLOR, argumentPassIn, ReasonFailure.INVALID_COLOR);
                }
            default:
                return new ArgumentError(argumentType, argumentPassIn + " (Type: " + argumentType.name() + ")", ReasonFailure.UNKNOWN_ARGUMENT_TYPE);
        }
    }

    private Object numberResult(float value)
    {
        int result = checkWithinRange(value);

        switch (result)
        {
            case 0:
                return value;
            case -1:
                return new ArgumentError(argumentType, value, ReasonFailure.VALUE_TOO_LOW);
            case 1:
                return new ArgumentError(argumentType, value, ReasonFailure.VALUE_TOO_HIGH);
            default:
                return new ArgumentError(argumentType, value, ReasonFailure.VALUE_CHECK_OUTSIDE_RANGE);
        }
    }

    private int checkWithinRange(float value)
    {
        if(minRange != maxRange)
        {
            if(value >= minRange && value <= maxRange)
            {
                return 0;
            }
            else
            {
                return value < minRange ? -1 : 1;
            }
        }

        return 0;
    }

    public ArgumentComponent setRange(float minRange, float maxRange)
    {
        this.minRange = minRange;
        this.maxRange = maxRange;

        return this;
    }

    public ArgumentComponent setCustomUsage(String custom)
    {
        this.customUsageName = custom;
        return this;
    }

    public <T extends Enum<T>> ArgumentComponent setCustomDataSet(Class<T> dataSet)
    {
        this.customDataSet = dataSet;
        return this;
    }

    public List<String> generateNumberRangeList()
    {
        if(minRange != Float.MIN_VALUE && maxRange != Float.MAX_VALUE)
        {
            List<String> numbers = new ArrayList<>();

            for (float number = minRange; number <= maxRange; number++)
            {
                numbers.add(String.valueOf(number));
            }

            return numbers;
        }

        return null;
    }

    public boolean isRequired()
    {
        return required;
    }

    public float getMinRange()
    {
        return minRange;
    }

    public float getMaxRange()
    {
        return maxRange;
    }

    public Class<?> getCustomDataSet() {
        return customDataSet;
    }

    public ArgumentType getArgumentType()
    {
        return argumentType;
    }

    public Object getInternalObjectClassification() {
        return internalObjectClassification;
    }

    @Override
    public String toString()
    {
        if(required)
        {
            return "<" + generateMeaning() + ">";
        }
        else
        {
            return "[" + generateMeaning() + "]";
        }
    }

    public String generateMeaning()
    {
        if(customUsageName == null)
        {
            switch(argumentType)
            {
                case FLOAT:
                    if(minRange != Float.MIN_VALUE || maxRange != Float.MAX_VALUE)
                        return minRange + " - " + maxRange;
                case INTEGER:
                    if(minRange != Float.MIN_VALUE || maxRange != Float.MAX_VALUE)
                        return (int)minRange + " - " + (int)maxRange;
                case ENUM:
                case PLAYER_NAME:
                case WORLD_NAME:
                case BOOLEAN:
                case STRING:
                case COLOR:
                default:
                    return argumentType.toString();
            }
        }
        else
        {
            return customUsageName;
        }
    }
}
