package com.sammurphy.sleepPercentage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.java.JavaPlugin;
import com.sammurphy.sleepPercentage.config.MessageConfig;
import com.sammurphy.sleepPercentage.lookups.ConfigMessageLookup;
import com.sammurphy.sleepPercentage.lookups.ConfigPermissionLookup;
import com.sammurphy.sleepPercentage.lookups.MessageComponents;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Sam Murphy on 25/05/2017.
 */
public class MessageInterrupter {

    /**
     * Used if Player is missing permission
     *
     * @param node Permission Node
     * @param plugin Plugin Root
     * @param sender Sender of Command
     * @param objects Object data of the command
     */
    public static void process(ConfigPermissionLookup.PermissionSubNodes node, JavaPlugin plugin, CommandSender sender, Object... objects)
    {
        String message;
        List<String> colors;

        if(node == null)
        {
            message = MessageConfig.self.getString(ConfigPermissionLookup.permissionLookup(ConfigPermissionLookup.PermissionSubNodes.DEFAULT_FALLBACK, MessageComponents.ERROR));
            colors = MessageConfig.self.getStringList(ConfigPermissionLookup.permissionLookup(ConfigPermissionLookup.PermissionSubNodes.DEFAULT_FALLBACK, MessageComponents.COLORS));
        }
        else
        {
            message = MessageConfig.self.getString(ConfigPermissionLookup.permissionLookup(node, MessageComponents.ERROR));
            colors = MessageConfig.self.getStringList(ConfigPermissionLookup.permissionLookup(node, MessageComponents.COLORS));
        }

        sender.sendMessage(generateMessage(message, colors, objects));
    }

    /**
     * Used for Changes to plugin with possible admin notification
     *
     * @param node Message Node
     * @param plugin Plugin Root
     * @param sender Sender of Command
     * @param objects Object data of the command
     */
	public static void process(ConfigMessageLookup.MessageSubNodes node, JavaPlugin plugin, CommandSender sender, Object... objects)
	{
		String message = MessageConfig.self.getString(ConfigMessageLookup.messageLookup(node, MessageComponents.MESSAGE));
		String adminMessage = MessageConfig.self.getString(ConfigMessageLookup.messageLookup(node, MessageComponents.ADMIN));
		List<String> colors = MessageConfig.self.getStringList(ConfigMessageLookup.messageLookup(node, MessageComponents.COLORS));

        sender.sendMessage(generateMessage(message, colors, objects));

        //TODO: Need to clean this string equals. I don't like it. If library output changes in future versions of Bukkit, then this will throw an error
        if(!adminMessage.equals("MessageConfig[path='', root='MessageConfig']"))
        {
            broadcastPermission(generateMessage(adminMessage, colors, objects), node.getAdminPermissionNode(), sender, plugin);
        }
	}

    /**
     * Used to broadcast message across an entire world
     *
     * @param node Message Node
     * @param plugin Plugin Root
     * @param world World to send message
     * @param objects Object data of the command
     */
    public static void process(ConfigMessageLookup.MessageSubNodes node, JavaPlugin plugin, World world, Object... objects)
    {
        String message = MessageConfig.self.getString(ConfigMessageLookup.messageLookup(node, MessageComponents.MESSAGE));
        List<String> colors = MessageConfig.self.getStringList(ConfigMessageLookup.messageLookup(node, MessageComponents.COLORS));

        String generatedMessage = generateMessage(message, colors, objects);
        world.getPlayers().forEach(player -> player.sendMessage(generatedMessage));
    }

    /**
     * Used for Global Broadcast
     *
     * @param node Message Node
     * @param plugin Plugin Root
     * @param objects Object data of the command
     */
    public static void process(ConfigMessageLookup.MessageSubNodes node, JavaPlugin plugin, Object... objects)
    {
        String message = MessageConfig.self.getString(ConfigMessageLookup.messageLookup(node, MessageComponents.MESSAGE));
        List<String> colors = MessageConfig.self.getStringList(ConfigMessageLookup.messageLookup(node, MessageComponents.COLORS));

        Bukkit.broadcastMessage(generateMessage(message, colors, objects));
    }

    private static void broadcastPermission(String message, String permission, CommandSender avoid, JavaPlugin plugin)
    {
        Set<CommandSender> recipients = new HashSet<>();
        for (Permissible permissible : plugin.getServer().getPluginManager().getPermissionSubscriptions(permission)) {
            if (permissible instanceof CommandSender && permissible.hasPermission(permission) && !permissible.equals(avoid)) {
                recipients.add((CommandSender) permissible);
            }
        }

        recipients.forEach(recipient -> recipient.sendMessage(message));
    }

	private static String generateMessage(String message, List<String> colors, Object[] objects)
    {
        StringBuilder newMessage = new StringBuilder();
        boolean inside = false;
        boolean var = false;
        for (char piece: message.toCharArray()) {
            switch (piece) {
                case '{':
                    inside = true;
                    continue;
                case '}':
                    inside = false;
                    continue;
                case '[':
                    var = true;
                    continue;
                case ']':
                    var = false;
                    continue;
            }

            if(inside)
            {
                int temp;
                try {
                    if((temp = Integer.parseInt(""+ piece)) < colors.size() && temp >= 0)
                    {
                        try {
                            ChatColor color = ChatColor.valueOf(colors.get(temp));
                            newMessage.append(color);
                        } catch (IllegalArgumentException ignored) { }
                    }
                } catch(NumberFormatException e) {
                    newMessage.append(piece);
                }

                inside = false;
            }
            else if(var)
            {
                try {
                    var = false;
                    int temp;
                    if((temp = Integer.parseInt(""+ piece)) < objects.length && temp >= 0)
                    {
                        newMessage.append(objects[temp].toString());
                    }
                } catch (NumberFormatException e) {
                    newMessage.append(piece);
                }
            }
            else
            {
                newMessage.append(piece);
            }
        }

        return newMessage.toString();
    }
}
