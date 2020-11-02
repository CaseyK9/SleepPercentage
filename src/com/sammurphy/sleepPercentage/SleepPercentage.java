package com.sammurphy.sleepPercentage;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.sammurphy.sleepPercentage.commands.CommandRegister;
import com.sammurphy.sleepPercentage.config.GeneralConfig;
import com.sammurphy.sleepPercentage.config.MessageConfig;
import com.sammurphy.sleepPercentage.eventManagement.BedEvents;
import com.sammurphy.sleepPercentage.eventManagement.PlayerEvents;
import com.sammurphy.sleepPercentage.eventManagement.WorldEvents;
import com.sammurphy.sleepPercentage.events.SleepPercentageOnDisable;
import com.sammurphy.sleepPercentage.lookups.ConfigLookup;
import com.sammurphy.sleepPercentage.lookups.ConfigMessageLookup;
import com.sammurphy.sleepPercentage.lookups.PermissionLookup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SleepPercentage extends JavaPlugin {

    public static final int START_BED_TIME = 12541;
    public static final int END_BED_TIME = 23458;
    public static final int LAST_DAY_TICK = 23999;

    public static final BuildType BUILD_TYPE = BuildType.DEVELOPMENT;
    public static final String VERSION_NUMBER = "1.3.0";
    public static final Object TARGET_VERSION_NUMBER = "1.13.1";

    public enum BuildType
    {
        STABLE,
        UNSTABLE,
        DEVELOPMENT
    }

    private final HashMap<UUID, ArrayList<UUID>> playersSleeping = new HashMap<>();

    @Override
    public void onEnable() {
        super.onEnable();

        LoggingHandler.setLogger(getLogger());
        LoggingHandler.setLogLevel(Level.ALL);

        new MessageConfig(this);
        new GeneralConfig(this);

        Timers.setPlugin(this);
        Timers.initCleaningProcess(GeneralConfig.self.getInt(ConfigLookup.cleaningInterval()));

        new CommandRegister(this);

        //Load Events
        PlayerEvents playerEvents = new PlayerEvents(this);
        new BedEvents(this, playerEvents);
        WorldEvents worldEvents = new WorldEvents(this);

        getServer().getWorlds().forEach(worldEvents::processWorld);

        //TODO: Need to work out how registered listeners work and add this for optimizations. May need to move to a by function registering
//        if(!GeneralConfig.self.getBoolean("sleepingEnabled"))
//            PlayerInteractEvent.getHandlerList().unregister((JavaPlugin)this);

//        Bukkit.getConsoleSender().getEffectivePermissions().stream().filter(x -> x.getPermission().startsWith("sp.receiveadmincommandmessage")).forEach(x -> System.out.println(x.getPermission()));
    }

    public void onDisable() {
        Timers.onDisable();

        getServer().getPluginManager().callEvent(new SleepPercentageOnDisable());

        super.onDisable();
    }

    public void testForSleepPercentage(UUID worldUID, World world) {
        float totalPlayers = getTotalPlayersForWorld(world);
        float totalPlayersRegardlessPermissions = playersSleeping.get(worldUID).size();

        if(totalPlayersRegardlessPermissions > totalPlayers)
            totalPlayers = totalPlayersRegardlessPermissions;

        float percentage = (totalPlayersRegardlessPermissions / totalPlayers) * 100;
        broadcastPercentageSleepingMessage(world, totalPlayers);
        if (percentage >= GeneralConfig.self.getInt(ConfigLookup.worldPercentage(world.getName()))) {
            world.setTime(world.getTime() + ((LAST_DAY_TICK - (world.getTime() % LAST_DAY_TICK)) + GeneralConfig.self.getLong(ConfigLookup.worldTimeSet(world.getName()))));

            if(GeneralConfig.self.getBoolean(ConfigLookup.worldChangeWeather(world.getName())))
            {
                if (world.hasStorm() || world.isThundering()) {
                    world.setThundering(false);
                    world.setStorm(false);
                    world.setWeatherDuration(GeneralConfig.self.getInt(ConfigLookup.worldWeatherTick(world.getName())));
                }
            }

            playersSleeping.get(worldUID).clear();
        }
    }

    public int getTotalPlayersForWorld(World world)
    {
        int total = 0;

        for (Player player : world.getPlayers()) {
            if(player.hasPermission(PermissionLookup.excludeSleepingChecksSpecific(world.getName())))
            {
                List<String> bypassGameModes = GeneralConfig.self.getStringList(ConfigLookup.worldExcludeGameModes(world.getName()));
                List<GameMode> gameModes = bypassGameModes.stream().map(GameMode::valueOf).collect(Collectors.toList());

                if(!gameModes.contains(player.getGameMode()))
                {
                    total++;
                }
            }
            else
            {
                total++;
            }
        }

        return total;
    }

    public void broadcastPercentageSleepingMessage(World world, float totalPlayers) {
        int percentage = (int) (playersSleeping.get(world.getUID()).size() / totalPlayers * 100);
        MessageInterrupter.process(ConfigMessageLookup.MessageSubNodes.TOTAL, this, world, Integer.toString(percentage),
                GeneralConfig.self.getInt(ConfigLookup.worldPercentage(world.getName())), world.getName());
    }

    public HashMap<UUID, ArrayList<UUID>> getPlayersSleeping() {
        return playersSleeping;
    }
}
