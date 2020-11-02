package com.sammurphy.sleepPercentage.lookups;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class ConfigPermissionLookup {
    private static String permissionRootTag()
    {
        return "permissions.";
    }

    private static String permissionCleaner(String node)
    {
        return node.replace(".", "") + ".";
    }

    private static String permissionDefaultFallback()
    {
        return permissionRootTag() + "defaultFallback.";
    }

    public enum PermissionSubNodes
    {
        SP_TOGGLE(PermissionLookup.toggle(), ConfigPermissionLookup::permissionToggle),
        SP_SET(PermissionLookup.set(), ConfigPermissionLookup::permissionSet),
        SP_DAY_TICK(PermissionLookup.dayTick(), ConfigPermissionLookup::permissionDaytick),
        SP_DISABLE(PermissionLookup.disable(), ConfigPermissionLookup::permissionDisable),
        DEFAULT_FALLBACK("DefaultFallback", ConfigPermissionLookup::permissionDefaultFallback),
        SLEEPING_IN_WORLD_WILDCARD("SleepingInWorldWildCard", ConfigPermissionLookup::permissionSleepingInWorldWildcard);

        private final String permissionNode;
        private final Supplier<String> functionPointer;

        PermissionSubNodes(String permissionNode, Supplier<String> functionPointer) {
            this.permissionNode = permissionNode;
            this.functionPointer = functionPointer;
        }

        public String getFunctionPointer() {
            return functionPointer.get();
        }

        public String getPermissionNode() {
            return permissionNode;
        }

        public String getPermissionNodeCondensed()
        {
            String temp = permissionNode.substring(0, 2).replace(".", "");
            return temp.substring(0, 1).toUpperCase() + temp.substring(1);
        }

        public static PermissionSubNodes nodeLookup(String permissionNode)
        {
            for (PermissionSubNodes node : PermissionSubNodes.values()) {
                if(node.getPermissionNode().equals(permissionNode))
                {
                    return node;
                }
            }

            return null;
        }
    }

    public static String permissionLookup(PermissionSubNodes permissionNode, MessageComponents messageComponents)
    {
        try {
            return (String) ConfigPermissionLookup.class.getDeclaredMethod("permission" + permissionNode.getPermissionNodeCondensed() + messageComponents.toString()).invoke(null);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            return "";
        }
    }

    private static String permissionToggle()
    {
        return permissionRootTag() + permissionCleaner(PermissionLookup.toggle());
    }

    public static String permissionToggleError()
    {
        return permissionToggle() + ConfigLookup.messageError();
    }

    public static String permissionToggleColor()
    {
        return permissionRootTag() + ConfigLookup.messageColor();
    }

    private static String permissionSet()
    {
        return permissionRootTag() + permissionCleaner(PermissionLookup.set());
    }

    public static String permissionSetError()
    {
        return permissionSet() + ConfigLookup.messageError();
    }

    public static String permissionSetColor()
    {
        return permissionRootTag() + ConfigLookup.messageColor();
    }

    private static String permissionDaytick()
    {
        return permissionRootTag() + permissionCleaner(PermissionLookup.dayTick());
    }

    public static String permissionDaytickError()
    {
        return permissionDaytick() + ConfigLookup.messageError();
    }

    public static String permissionDaytickColor()
    {
        return permissionRootTag() + ConfigLookup.messageColor();
    }

    private static String permissionDisable()
    {
        return permissionRootTag() + permissionCleaner(PermissionLookup.disable());
    }

    public static String permissionDisableError()
    {
        return permissionDisable() + ConfigLookup.messageError();
    }

    public static String permissionDisableColor()
    {
        return permissionRootTag() + ConfigLookup.messageColor();
    }

    private static String permissionSleepingInWorldWildcard()
    {
        return permissionRootTag() + "sleepingInWorldWildcard.";
    }

    public static String permissionSleepingInWorldWildcardError()
    {
        return permissionSleepingInWorldWildcard() + ConfigLookup.messageError();
    }

    public static String permissionSleepingInWorldWildcardColor()
    {
        return permissionRootTag() + ConfigLookup.messageColor();
    }
}
