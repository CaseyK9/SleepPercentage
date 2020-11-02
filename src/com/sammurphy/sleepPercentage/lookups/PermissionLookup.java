package com.sammurphy.sleepPercentage.lookups;

public class PermissionLookup {

    private static String rootNode()
    {
        return "sp.";
    }

    public static String set()
    {
        return rootNode() + "set";
    }

    public static String toggle()
    {
        return rootNode() + "state";
    }

    public static String dayTick()
    {
        return rootNode() + "daytick";
    }

    public static String disable()
    {
        return rootNode() + "enabled";
    }

    public static String timeInOtherWorld()
    {
        return rootNode() + "timeInOtherWorld";
    }

    private static String sleepNearMob()
    {
        return rootNode() + "sleepWhileMobNear.";
    }

    private static String sleepNearMobWorld()
    {
        return rootNode() + sleepNearMob() + "world.";
    }

    public static String sleepNearMobWorldWildCard()
    {
        return sleepNearMobWorld() + "*";
    }

    public static String sleepNearMobWorldSpecific(String worldName)
    {
        return sleepNearMobWorld() + worldNamePermissionConverter(worldName);
    }

    private static String sleepInWorld()
    {
        return rootNode() + "sleepingInWorld.worlds.";
    }

    public static String sleepInWorldWildCard()
    {
        return sleepInWorld() + "*";
    }

    public static String sleepInWorldSpecific(String worldName)
    {
        return sleepInWorld() + worldNamePermissionConverter(worldName);
    }

    private static String excludeSleepingChecks()
    {
        return rootNode() + "excludeFromSleepingChecks.worlds.";
    }

    public static String excludeSleepingChecksWildCard()
    {
        return excludeSleepingChecks() + "*";
    }

    public static String excludeSleepingChecksSpecific(String worldName)
    {
        return excludeSleepingChecks() + worldNamePermissionConverter(worldName);
    }

    public static String worldNamePermissionConverter(String name)
    {
        return name.trim();
    }

    //Messages

    private static String receiveAdminCommands()
    {
        return rootNode() + "receiveAdminCommandMessage.";
    }

    public static String messageWildCard()
    {
        return receiveAdminCommands() + "*";
    }

    public static String messageSleepPercentageEnable()
    {
        return receiveAdminCommands() + "sleepPercentageEnable";
    }

    public static String messageSleepPercentageDisable()
    {
        return receiveAdminCommands() + "sleepPercentageDisable";
    }

    public static String messageSetWorldPercentage()
    {
        return receiveAdminCommands() + "setWorldPercentage";
    }

    public static String messageSetWorldDayTick()
    {
        return receiveAdminCommands() + "setWorldDayTick";
    }

    public static String messageSetWorldDisabled()
    {
        return receiveAdminCommands() + "setWorldDisabled";
    }

    public static String messageSleepPercentageChangeState() {
        return receiveAdminCommands() + "sleepPercentageStatus";
    }

    public static String messageAdjustTime() {
        return receiveAdminCommands() + "changeTime";
    }
}
