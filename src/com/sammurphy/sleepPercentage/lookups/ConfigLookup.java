package com.sammurphy.sleepPercentage.lookups;

public class ConfigLookup {

    //General Config
    public static String enabled()
    {
        return "sleepingEnabled";
    }

    public static String cleaningInterval()
    {
        return "cleaningInterval";
    }

    public static String overrloadTimeCommand()
    {
        return "overloadTimeCommand";
    }

    //World Config
    public static String worldDisable(String worldName)
    {
        return worldRootTag() + worldNameConverter(worldName) + ".disabled";
    }

    public static String worldPercentage(String worldName)
    {
        return worldRootTag() + worldNameConverter(worldName) + ".percentage";
    }

    public static String worldTimeSet(String worldName)
    {
        return worldRootTag() + worldNameConverter(worldName) + ".timeset";
    }

    public static String worldSleepingPermission(String worldName)
    {
        return worldRootTag() + worldNameConverter(worldName) + ".sleepingPermission";
    }

    public static String worldWeatherTick(String worldName)
    {
        return worldRootTag() + worldNameConverter(worldName) + ".maxtickweatherstay";
    }

    public static String worldChangeWeather(String worldName)
    {
        return worldRootTag() + worldNameConverter(worldName) + ".changeWeatherToClear";
    }

    public static String worldMobNearbyEnabled(String worldName)
    {
        return worldRootTag() + worldNameConverter(worldName) + mobNearbyRootTag() + "enabled";
    }

    public static String worldMobNearbyDelay(String worldName)
    {
        return worldRootTag() + worldNameConverter(worldName) + mobNearbyRootTag() + "delay";
    }

    public static String worldMobNearbyNameTag(String worldName)
    {
        return worldRootTag() + worldNameConverter(worldName) + mobNearbyRootTag() + "nameTagOnly";
    }

    public static String worldMobNearbyIgnorePermission(String worldName)
    {
        return worldRootTag() + worldNameConverter(worldName) + mobNearbyRootTag() + "ignorePermission";
    }

    public static String worldExcludeGameModes(String worldName)
    {
        return worldRootTag() + worldNameConverter(worldName) + ".excludeGameModes";
    }

    private static String worldRootTag()
    {
        return "percentageperworld.";
    }

    private static String mobNearbyRootTag()
    {
        return ".allowPlayersToSleepWhileMobsAreNearby.";
    }

    public static String worldNameConverter(String name)
    {
        return name;
    }

    public static String messageColor()
    {
        return "color";
    }

    public static String messageMessage()
    {
        return "message";
    }

    public static String messageAdmin()
    {
        return "admin";
    }

    public static String messageError()
    {
        return "error";
    }
}
