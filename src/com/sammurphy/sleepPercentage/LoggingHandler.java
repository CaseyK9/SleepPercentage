package com.sammurphy.sleepPercentage;

import org.bukkit.Bukkit;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingHandler {

    private static Logger logger;

    private static Level currentLogLevel;

    private static File fileLocation;

    public static void log(Level logLevel, String message)
    {
        if(fileLocation == null)
        {
            fileLocation = new File(Bukkit.getServer().getPluginManager().getPlugin("SleepPercentage").getDataFolder(), "logs");
        }

        if(logLevel.intValue() >= currentLogLevel.intValue())
        {
            if(logger != null)
            {
                logger.log(logLevel, message);
            }

//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            if(!new NonNullableArrayList<File>(Arrays.asList(Objects.requireNonNull(fileLocation.listFiles()))).forEach(o -> o.getName().equals(dateFormat.format(new Date(System.nanoTime())) + ".log")))
        }
    }

    public static void info(String message)
    {
        log(Level.INFO, message);
    }

    public static void severe(String message)
    {
        log(Level.SEVERE, message);
    }

    public static void warning(String message)
    {
        log(Level.WARNING, message);
    }

    public static void config(String message)
    {
        log(Level.CONFIG, message);
    }

    public static void setLogger(Logger logger)
    {
        LoggingHandler.logger = logger;
    }

    public static void setLogLevel(Level logLevel)
    {
        currentLogLevel = logLevel;
    }
}
