package com.sammurphy.sleepPercentage.commandSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class ArgumentMatchContainer
{
    private final Object[] returnedResults;
    private final ArgumentComponent[] components;
    private final ArgumentsProvider argumentsProvider;

    public ArgumentMatchContainer(Object[] returnedResults, ArgumentComponent[] components, ArgumentsProvider argumentsProvider)
    {
        this.returnedResults = returnedResults;
        this.components = components;
        this.argumentsProvider = argumentsProvider;
    }

    public boolean isPassable()
    {
        return Arrays.stream(returnedResults).noneMatch(obj -> obj instanceof ArgumentError);
    }

    public float calculateScore(int totalArguments)
    {
        float modifier = 1.0f / ((float)totalArguments);
        float score = 1.0f;

        if(totalArguments > components.length)
        {
            return score * -1;
        }

        for (int i = 0; i < totalArguments - 1; ++i)
        {
            if(returnedResults[i] instanceof ArgumentError)
            {
                score -= modifier;
            }
        }

        for(int i = totalArguments - 1; i < components.length; ++i)
        {
            switch(components[i].getArgumentType())
            {
                //TODO: String needs to check for custom tab usage
                case STRING:
                case FLOAT:
                case INTEGER:
                    if(returnedResults[i] instanceof OptionalArgument)
                    {
                        continue;
                    }
                    else
                    {
                        score -= modifier;
                        i = components.length;
                        break;
                    }
                case PLAYER_NAME:
                case WORLD_NAME:
                case BOOLEAN:
                case ENUM:
                case COLOR:
                    break;
            }
        }

//        if(score == 1.0f) score = -1;

        return score;
    }

    public float calculateScore()
    {
        float totalComponentsNonOptional = 0;
        float totalPassedNonOptional = 0;

        for (Object obj : returnedResults)
        {
            if(!(obj instanceof OptionalArgument))
            {
                totalComponentsNonOptional++;
                if(!(obj instanceof ArgumentError))
                {
                    totalPassedNonOptional++;
                }
            }
        }

        return totalPassedNonOptional / totalComponentsNonOptional;
    }

    public int getPassableArgumentsCount()
    {
        int total = 0;

        for (Object obj : returnedResults)
        {
            if(!(obj instanceof OptionalArgument) && !(obj instanceof ArgumentError))
            {
                total++;
            }
        }

        return total;
    }

    public int getArgumentCount()
    {
        //TODO: See if I can make an algorithm for optionals adding increased difficulty to processing
        return Arrays.stream(components).filter(ArgumentComponent::isRequired).collect(Collectors.toList()).size();
    }

    public int differenceCount(int totalPassedArgumentCount)
    {
        return Math.abs(components.length - totalPassedArgumentCount);
    }

    public Object[] getReturnedResults()
    {
        return returnedResults;
    }

    public ArgumentComponent[] getComponents()
    {
        return components;
    }

    public ArgumentsProvider getArgumentsProvider()
    {
        return argumentsProvider;
    }

    public List<String> generateErrors()
    {
        ArrayList<String> errors = new ArrayList<>();

        int errorCounter = 0;
        for (Object returnedResult : returnedResults) {
            if (returnedResult instanceof ArgumentError) {
                errors.add(((ArgumentError) returnedResult).toString(errorCounter++));
            }
        }

        return errors;
    }

    @Override
    public String toString() {
        return "ArgumentMatchContainer{" +
                "returnedResults=" + Arrays.toString(returnedResults) +
                ", components=" + Arrays.toString(components) +
                ", argumentsProvider=" + argumentsProvider +
                '}';
    }
}
