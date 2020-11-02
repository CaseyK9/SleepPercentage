package com.sammurphy.sleepPercentage.commandSystem;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public final class ArgumentsProvider
{
    private final CommandSenderType commandSenderType;
    private final ArgumentComponent[] argumentComponents;

    public ArgumentsProvider(CommandSenderType commandSenderType, ArgumentComponent... argumentComponents)
    {
        this.commandSenderType = commandSenderType;
        this.argumentComponents = argumentComponents;
    }

    public ArgumentMatchContainer processCommand(CommandSender commandSender, String[] arguments)
    {
        switch(commandSenderType)
        {
            case CONSOLE:
                if(commandSender instanceof Player)
                {
                    return null;
                }
                break;
            case PLAYER:
                if(!(commandSender instanceof Player))
                {
                    return null;
                }
                break;
		case BOTH:
			break;
		default:
			break;
        }

        Object[] returnResults = new Object[argumentComponents.length];

        //TODO: Per Provider Permissions
//        if(permissionNode != null && !permissionNode.isEmpty())
//        {
//            if(!commandSender.hasPermission(permissionNode))
//            {
//                returnResults = new Object[] {
//                        new ArgumentError()
//                };
//            }
//        }

        int argumentCount = 0;
        for(int i = 0; i < argumentComponents.length; i++)
        {
            boolean isRequired = argumentComponents[i].isRequired();

            if(argumentCount >= arguments.length)
            {
                if(!isRequired)
                {
                    returnResults[i] = new OptionalArgument(new ArgumentError(argumentComponents[i].getArgumentType(), null, ReasonFailure.NOT_ENOUGH_ARGUMENTS));
                }
                else
                {
                    returnResults[i] = new ArgumentError(argumentComponents[i].getArgumentType(), null, ReasonFailure.NOT_ENOUGH_ARGUMENTS);
                }
                argumentCount++;
                continue;
            }

            Object obj;
            if(!((obj = argumentComponents[i].passArgumentChecks(arguments[argumentCount])) instanceof ArgumentError))
            {
                returnResults[i] = obj;
                argumentCount++;
            }
            else if(!isRequired)
            {
                returnResults[i] = new OptionalArgument((ArgumentError) obj);
            }
            else
            {
                returnResults[i] = obj;
                argumentCount++;
            }
        }

        return new ArgumentMatchContainer(returnResults, argumentComponents, this);
    }

    public String toString(Object[] results)
    {
        StringBuilder output = new StringBuilder();

        int errorCounter = 0;
        for(int i = 0; i < argumentComponents.length; i++)
        {
            output.append(!(results[i] instanceof ArgumentError) ? ChatColor.GOLD
                : ErrorColor.getErrorColor(errorCounter++));

            if(results[i] instanceof ArgumentError || results[i] instanceof OptionalArgument)
            {
                output.append(argumentComponents[i].toString());
            }
            else
            {
                output.append(objectToNameConvert(results[i].toString()));
            }

            output.append(" ");
        }

        return output.toString();
    }

    public String generateGenericUsage(String commandNode, CommandSenderType senderType)
    {
        if(commandSenderType != CommandSenderType.BOTH && (commandSenderType != senderType && senderType != CommandSenderType.BOTH))
        {
            return null;
        }

        StringBuilder builder = new StringBuilder("/" + commandNode);

        for (int i = 0; i < argumentComponents.length; i++)
        {
            builder.append(" " + ErrorColor.getErrorColor(i) + argumentComponents[i]);
        }

        return builder.toString();
    }

    private String objectToNameConvert(Object obj)
    {
        if(obj instanceof World)
        {
            return ((World) obj).getName();
        }
        else
            return obj.toString();
    }

    public void setPermissionNode(String permissionNode)
    {
    }

    @Override
    public String toString() {
        return "ArgumentsProvider{" +
                "commandSenderType=" + commandSenderType +
                ", argumentComponents=" + Arrays.toString(argumentComponents) +
                '}';
    }
}