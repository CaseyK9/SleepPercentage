package com.sammurphy.sleepPercentage.commandSystem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.sammurphy.sleepPercentage.LoggingHandler;
import com.sammurphy.sleepPercentage.MessageInterrupter;
import com.sammurphy.sleepPercentage.NonNullableArrayList;
import com.sammurphy.sleepPercentage.lookups.ConfigPermissionLookup;

import java.util.*;
import java.util.stream.Collectors;

public abstract class CommandTool implements CommandExecutor, TabCompleter {

    protected final JavaPlugin plugin;

    private final String commandNode;
    private final ArgumentsProvider[] argumentsProviders;

    private final PermissionsMeta permissionsMeta = new PermissionsMeta();

    protected CommandTool(JavaPlugin plugin, String commandNode, ArgumentsProvider... providers) {
        this.plugin = plugin;
        this.commandNode = commandNode;
        argumentsProviders = providers;

        plugin.getCommand(commandNode).setExecutor(this);
        plugin.getCommand(commandNode).setTabCompleter(this);

        LoggingHandler.config("Command Registered: " + commandNode);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args)
    {
        //Permission Check
        if(!permissionsMeta.bypassCommandPermissionCheck && ((permissionsMeta.useDefaultPermission && !command.testPermissionSilent(commandSender)) ||
                (!permissionsMeta.useDefaultPermission && !commandSender.hasPermission(getPermissionNode()))))
        {
            MessageInterrupter.process(ConfigPermissionLookup.PermissionSubNodes.nodeLookup(getPermissionNode()), plugin, commandSender);
            return true;
        }

        //Command Level Sender Check
        CommandSenderType commandSenderType;
        if(commandSender instanceof Player)
        {
            commandSenderType = CommandSenderType.PLAYER;
        }
        else
        {
            commandSenderType = CommandSenderType.CONSOLE;
        }

        switch(getCommandSenderType())
        {
            case CONSOLE:
                if(commandSender instanceof Player)
                {
                    commandSender.sendMessage(ReasonFailure.SENDER_NEEDS_TO_BE_CONSOLE.toString());
                    return true;
                }
                break;
            case PLAYER:
                if(!(commandSender instanceof Player))
                {
                    commandSender.sendMessage(ReasonFailure.SENDER_NEEDS_TO_BE_PLAYER.toString());
                    return true;
                }
                break;
		case BOTH:
			break;
		default:
			break;
        }

        //Argument Lengths Check
        if(argumentsProviders.length == 0)
        {
            onCommand(commandSender, command, null);
            return true;
        }

        //Handle Usages
        if(args.length > 0 && args[0].equals("?"))
        {
            NonNullableArrayList<String> genericUsages = new NonNullableArrayList<>();

            for (ArgumentsProvider provider : argumentsProviders) {
                genericUsages.add(provider.generateGenericUsage(commandNode, commandSenderType));
            }

            commandSender.sendMessage(genericUsages.toArray(new String[0]));
            return true;
        }

        //Process Argument Patterns
        ArgumentMatchContainer[] argumentMatchContainers = new ArgumentMatchContainer[argumentsProviders.length];
        for(int i = 0; i < argumentMatchContainers.length; i++)
        {
            argumentMatchContainers[i] = argumentsProviders[i].processCommand(commandSender, args);
        }

        argumentMatchContainers = trimArray(argumentMatchContainers);

        System.out.println(Arrays.toString(argumentMatchContainers));

        //Calculate Scoring of Matches, then sort them
        List<ArgumentMatchContainer> perfectScores = new ArrayList<>();
        List<ArgumentMatchContainer> sortingList = new ArrayList<>();
        for (ArgumentMatchContainer argumentMatchContainer : argumentMatchContainers) {
            if (argumentMatchContainer == null) {
                continue;
            }

            if (argumentMatchContainer.isPassable()) {
                perfectScores.add(argumentMatchContainer);
            } else {
                sortingList.add(argumentMatchContainer);
            }
        }

        //Check if any matches are perfect matches
        if(perfectScores.size() > 0)
        {
            perfectScores.sort(Comparator.comparing(ArgumentMatchContainer::getPassableArgumentsCount));

            if(perfectScores.size() > 1)
            {
                for(int i = 0; i < perfectScores.size();)
                {
                    boolean optionalAndRequiredMix = false;
                    for (ArgumentComponent component : perfectScores.get(i).getComponents()) {
                        if(component.isRequired())
                        {
                            optionalAndRequiredMix = true;
                        }
                    }

                    if(!optionalAndRequiredMix)
                    {
                        perfectScores.remove(i);
                    }
                    else
                    {
                        i++;
                    }
                }
            }

            //TODO: Execute Command off this
            onCommand(commandSender, command, perfectScores.get(perfectScores.size() - 1));
        }
        //Find the most likely match.
        else
        {
            List<ArgumentMatchContainer> containers;
            int differenceFactor = 0;
            do {
                containers = getMatchedDifferenceContainer(sortingList, args.length, differenceFactor++);
            } while(containers.size() == 0);

            containers.sort(Comparator.comparing(ArgumentMatchContainer::getArgumentCount));

            ArgumentMatchContainer matchedContainer = containers.get(0);
            List<String> errors = matchedContainer.generateErrors();
            String usage = matchedContainer.getArgumentsProvider().toString(matchedContainer.getReturnedResults());
            errors.add(0, ChatColor.WHITE + "/" + alias + " " + usage);

            commandSender.sendMessage(errors.toArray(new String[errors.size()]));
        }

        return true;
    }

    private ArgumentMatchContainer[] trimArray(ArgumentMatchContainer[] matchContainers)
    {
        NonNullableArrayList<Object> containers = new NonNullableArrayList<>();
        containers.addAll(Arrays.asList(matchContainers));

        return containers.toArray(new ArgumentMatchContainer[containers.size()]);
    }

    private List<ArgumentMatchContainer> getMatchedDifferenceContainer(List<ArgumentMatchContainer> containers, int totalSize, int difference)
    {
        List<ArgumentMatchContainer> matchContainers = new ArrayList<>();
        for (ArgumentMatchContainer container : containers) {
            if(container.differenceCount(totalSize) == difference)
            {
                matchContainers.add(container);
            }
        }

        return matchContainers;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args)
    {
        switch(getCommandSenderType())
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

        if(argumentsProviders.length == 0)
        {
            return null;
        }

        ArgumentMatchContainer[] argumentMatchContainers = new ArgumentMatchContainer[argumentsProviders.length];
        for(int i = 0; i < argumentMatchContainers.length; i++)
        {
            argumentMatchContainers[i] = argumentsProviders[i].processCommand(commandSender, args);
        }

        List<ArgumentMatchContainer> sortingList = new ArrayList<>();
        for (ArgumentMatchContainer argumentMatchContainer : argumentMatchContainers) {
            if (argumentMatchContainer == null) {
                continue;
            }

            sortingList.add(argumentMatchContainer);
        }

        sortingList.sort((o1, o2) -> o1.calculateScore(args.length) < o2.calculateScore(args.length) ? 1 : -1);

        ArgumentMatchContainer container = sortingList.get(0);
        ArgumentComponent component = container.getComponents()[args.length - 1];

        List<String> returnedData = new ArrayList<>();
        String argumentStarter = args[args.length - 1];
        switch(component.getArgumentType())
        {
            case PLAYER_NAME:
                for(Player player : Bukkit.getOnlinePlayers())
                {
                    if(argumentStarter.equals(""))
                    {
                        returnedData.add(player.getName());
                    }
                    else
                    {
                        if(player.getName().toLowerCase().startsWith(argumentStarter.toLowerCase()))
                        {
                            returnedData.add(player.getName());
                        }
                    }
                }
                return returnedData;
            case WORLD_NAME:
                for(World world : Bukkit.getWorlds())
                {
                    if(argumentStarter.equals(""))
                    {
                        returnedData.add(world.getName());
                    }
                    else
                    {
                        if(world.getName().toLowerCase().startsWith(argumentStarter.toLowerCase()))
                        {
                            returnedData.add(world.getName());
                        }
                    }
                }
                return returnedData;
            case BOOLEAN:
                if(argumentStarter.equals(""))
                {
                    returnedData.add("true");
                    returnedData.add("false");
                }
                else
                {
                    if("false".startsWith(argumentStarter.toLowerCase()))
                    {
                        returnedData.add("false");
                    }

                    if("true".startsWith(argumentStarter.toLowerCase()))
                    {
                        returnedData.add("true");
                    }
                }
            case STRING:
                List<String> customTabData = getTabbedArguments(component, args.length - 1);
                if(customTabData == null)
                {
                    return null;
                }
                else if(argumentStarter.equals(""))
                {
                    return customTabData;
                }
                else
                {
                    return customTabData.stream().filter(customTabDatum -> customTabDatum.toLowerCase()
                            .startsWith(argumentStarter.toLowerCase())).collect(Collectors.toList());
                }
            case ENUM:
                return Arrays.<Object>stream(component.getCustomDataSet().getEnumConstants())
                        .filter(o -> o.toString().toLowerCase().contains(argumentStarter.toLowerCase()))
                        .map(Object::toString).collect(Collectors.toList());
            case COLOR:
                return Arrays.<Object>stream(ChatColor.values())
                        .filter(o -> o.toString().contains(argumentStarter.toUpperCase()))
                        .map(Object::toString).collect(Collectors.toList());
            case FLOAT:
            case INTEGER:
                return component.generateNumberRangeList();
            default:
                return null;
        }
    }

    public final String getCommandName()
    {
        return commandNode;
    }

    protected abstract CommandSenderType getCommandSenderType();

    protected abstract void onCommand(CommandSender sender, Command command, ArgumentMatchContainer args);

    protected abstract String getPermissionNode();

    protected abstract List<String> getTabbedArguments(ArgumentComponent component, int tabbedIndexCount);

    public PermissionsMeta getPermissionsMeta() {
        return permissionsMeta;
    }

    class PermissionsMeta
    {
        private boolean bypassCommandPermissionCheck = false;
        private boolean useDefaultPermission = true;

        public boolean isBypassCommandPermissionCheck() {
            return bypassCommandPermissionCheck;
        }

        public void setBypassCommandPermissionCheck(boolean bypassCommandPermissionCheck) {
            this.bypassCommandPermissionCheck = bypassCommandPermissionCheck;
        }

        public boolean isUseDefaultPermission() {
            return useDefaultPermission;
        }

        public void setUseDefaultPermission(boolean useDefaultPermission) {
            this.useDefaultPermission = useDefaultPermission;
        }
    }
}
