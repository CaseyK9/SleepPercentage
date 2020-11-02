package com.sammurphy.sleepPercentage.lookups;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class ConfigMessageLookup {
    public enum MessageSubNodes
    {
//        ENABLED("Enabled", PermissionLookup::messageSleepPercentageEnable),
//        DISABLED("Disabled", PermissionLookup::messageSetWorldDisabled),
        PLUGIN_CHANGE_STATE("PluginChangeState", PermissionLookup::messageSleepPercentageChangeState),
        SET("Set", PermissionLookup::messageSetWorldPercentage),
        SET_STATUS("SetStatus", PermissionLookup::messageSetWorldPercentage),
        NOT_SLEEPING("NotSleeping", null),
        SLEEPING("Sleeping", null),
        TOTAL("Total", null),
        DAY_TICK("DayTick", PermissionLookup::messageSetWorldDayTick),
        DAY_TICK_VALUE("DayTickValue", PermissionLookup::messageSetWorldDayTick),
        WORLD_DISABLE("WorldDisable", PermissionLookup::messageSetWorldDisabled),
        WORLD_DISABLE_STATUS("WorldDisableStatus", PermissionLookup::messageSetWorldDisabled),
        WORLD_DISABLE_FAIL("WorldDisableFail", null),
        TIME_IN_WORLD("TimeInWorld", null),
        PLUGIN_STATE("PluginState", null),
        ADJUST_TIME("AdjustTime", PermissionLookup::messageAdjustTime);

        private final String functionName;
        private final Supplier<String> adminPermissionNode;

        MessageSubNodes(String functionName, Supplier<String> adminPermissionNode) {
            this.functionName = functionName;
            this.adminPermissionNode = adminPermissionNode;
        }

        public String getAdminPermissionNode() {
            if(adminPermissionNode == null)
                return null;
            return adminPermissionNode.get();
        }

        @Override
        public String toString() {
            return functionName;
        }
    }

    public static String messageLookup(MessageSubNodes messageNode, MessageComponents messageComponents)
    {
        try {
            return (String) ConfigMessageLookup.class.getDeclaredMethod("message" + messageNode.toString() + messageComponents.toString()).invoke(null);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            return "";
        }
    }

    private static String messageEnabled()
    {
        return "enabled.";
    }

    public static String messageEnabledColor()
    {
        return messageRootTag() + messageEnabled() + ConfigLookup.messageColor();
    }

    public static String messageEnabledMessage()
    {
        return messageRootTag() + messageEnabled() + ConfigLookup.messageMessage();
    }

    public static String messageEnabledAdmin()
    {
        return messageRootTag() + messageEnabled() + ConfigLookup.messageAdmin();
    }

    private static String messageDisabled()
    {
        return "disabled.";
    }

    public static String messageDisabledColor()
    {
        return messageRootTag() + messageDisabled() + ConfigLookup.messageColor();
    }

    public static String messageDisabledMessage()
    {
        return messageRootTag() + messageDisabled() + ConfigLookup.messageMessage();
    }

    public static String messageDisabledAdmin()
    {
        return messageRootTag() + messageDisabled() + ConfigLookup.messageAdmin();
    }

    private static String messageSet()
    {
        return "set.";
    }

    public static String messageSetColor()
    {
        return messageRootTag() + messageSet() + ConfigLookup.messageColor();
    }

    public static String messageSetMessage()
    {
        return messageRootTag() + messageSet() + ConfigLookup.messageMessage();
    }

    public static String messageSetAdmin()
    {
        return messageRootTag() + messageSet() + ConfigLookup.messageAdmin();
    }

    private static String messageSetStatus()
    {
        return "setStatus.";
    }

    public static String messageSetStatusColor()
    {
        return messageRootTag() + messageSetStatus() + ConfigLookup.messageColor();
    }

    public static String messageSetStatusMessage()
    {
        return messageRootTag() + messageSetStatus() + ConfigLookup.messageMessage();
    }

    private static String messageNotSleeping()
    {
        return "notSleeping.";
    }

    public static String messageNotSleepingColor()
    {
        return messageRootTag() + messageNotSleeping() + ConfigLookup.messageColor();
    }

    public static String messageNotSleepingMessage()
    {
        return messageRootTag() + messageNotSleeping() + ConfigLookup.messageMessage();
    }

    private static String messageSleeping()
    {
        return "sleeping.";
    }

    public static String messageSleepingColor()
    {
        return messageRootTag() + messageSleeping() + ConfigLookup.messageColor();
    }

    public static String messageSleepingMessage()
    {
        return messageRootTag() + messageSleeping() + ConfigLookup.messageMessage();
    }

    private static String messageTotal()
    {
        return "total.";
    }

    public static String messageTotalColor()
    {
        return messageRootTag() + messageTotal() + ConfigLookup.messageColor();
    }

    public static String messageTotalMessage()
    {
        return messageRootTag() + messageTotal() + ConfigLookup.messageMessage();
    }

    private static String messageDayTick()
    {
        return "daytick.";
    }

    public static String messageDayTickColor()
    {
        return messageRootTag() + messageDayTick() + ConfigLookup.messageColor();
    }

    public static String messageDayTickMessage()
    {
        return messageRootTag() + messageDayTick() + ConfigLookup.messageMessage();
    }

    public static String messageDayTickAdmin()
    {
        return messageRootTag() + messageDayTick() + ConfigLookup.messageAdmin();
    }

    private static String messageDayTickValue()
    {
        return "daytickValue.";
    }

    public static String messageDayTickValueColor()
    {
        return messageRootTag() + messageDayTickValue() + ConfigLookup.messageColor();
    }

    public static String messageDayTickValueMessage()
    {
        return messageRootTag() + messageDayTickValue() + ConfigLookup.messageMessage();
    }

    private static String messageWorldDisable()
    {
        return "worldEnabled.";
    }

    public static String messageWorldDisableColor()
    {
        return messageRootTag() + messageWorldDisable() + ConfigLookup.messageColor();
    }

    public static String messageWorldDisableMessage()
    {
        return messageRootTag() + messageWorldDisable() + ConfigLookup.messageMessage();
    }

    public static String messageWorldDisableAdmin()
    {
        return messageRootTag() + messageWorldDisable() + ConfigLookup.messageAdmin();
    }

    private static String messageWorldDisableStatus()
    {
        return "worldEnabledStatus.";
    }

    public static String messageWorldDisableStatusColor()
    {
        return messageRootTag() + messageWorldDisableStatus() + ConfigLookup.messageColor();
    }

    public static String messageWorldDisableStatusMessage()
    {
        return messageRootTag() + messageWorldDisableStatus() + ConfigLookup.messageMessage();
    }

    private static String messageWorldDisableFailed()
    {
        return "worldDisableFailed.";
    }

    public static String messageWorldDisableFailedColor()
    {
        return messageRootTag() + messageWorldDisableFailed() + ConfigLookup.messageColor();
    }

    public static String messageWorldDisableFailedMessage()
    {
        return messageRootTag() + messageWorldDisableFailed() + ConfigLookup.messageMessage();
    }

    private static String messageNotAllowedToSleep()
    {
        return "notAllowedToSleep.";
    }

    public static String messageNotAllowedToSleepColor()
    {
        return messageRootTag() + messageNotAllowedToSleep() + ConfigLookup.messageColor();
    }

    public static String messageNotAllowedToSleepMessage()
    {
        return messageRootTag() + messageNotAllowedToSleep() + ConfigLookup.messageMessage();
    }

    private static String messageTimeInWorld()
    {
        return "timeInWorld.";
    }

    public static String messageTimeInWorldColor()
    {
        return messageRootTag() + messageTimeInWorld() + ConfigLookup.messageColor();
    }

    public static String messageTimeInWorldMessage()
    {
        return messageRootTag() + messageTimeInWorld() + ConfigLookup.messageMessage();
    }

    private static String messagePluginState()
    {
        return "pluginState.";
    }

    public static String messagePluginStateColor()
    {
        return messageRootTag() + messagePluginState() + ConfigLookup.messageColor();
    }

    public static String messagePluginStateMessage()
    {
        return messageRootTag() + messagePluginState() + ConfigLookup.messageMessage();
    }

    private static String messagePluginChangeState()
    {
        return "pluginChangeState.";
    }

    public static String messagePluginChangeStateColor()
    {
        return messageRootTag() + messagePluginChangeState() + ConfigLookup.messageColor();
    }

    public static String messagePluginChangeStateMessage()
    {
        return messageRootTag() + messagePluginChangeState() + ConfigLookup.messageMessage();
    }

    public static String messagePluginChangeStateAdmin()
    {
        return messageRootTag() + messagePluginChangeState() + ConfigLookup.messageAdmin();
    }

    private static String messageAdjustTime()
    {
        return "adjustTime.";
    }

    public static String messageAdjustTimeColor()
    {
        return messageRootTag() + messageAdjustTime() + ConfigLookup.messageColor();
    }

    public static String messageAdjustTimeMessage()
    {
        return messageRootTag() + messageAdjustTime() + ConfigLookup.messageMessage();
    }

    public static String messageAdjustTimeAdmin()
    {
        return messageRootTag() + messageAdjustTime() + ConfigLookup.messageAdmin();
    }

    private static String messageRootTag()
    {
        return "messages.";
    }

}
