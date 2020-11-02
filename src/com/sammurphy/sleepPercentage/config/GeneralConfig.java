package com.sammurphy.sleepPercentage.config;

import org.bukkit.plugin.java.JavaPlugin;
import com.sammurphy.sleepPercentage.LoggingHandler;
import com.sammurphy.sleepPercentage.lookups.ConfigMessageLookup;

public class GeneralConfig extends ConfigBase {

    public static GeneralConfig self;

    public GeneralConfig(JavaPlugin plugin) {
        super(plugin);

        self = this;

        set("percent", null);
        set("timeset", null);
        set("version", null);
        set("maxtickweatherstay", null);
        set("worldDisableFailed", null);

        //1.3.0 Conversion
        if(contains("messages.enabled"))
        {
            MessageConfig.self.set(ConfigMessageLookup.messageEnabledMessage(), getString("messages.enabled.message"));
            MessageConfig.self.set(ConfigMessageLookup.messageEnabledColor(), getList("messages.enabled.color"));
            LoggingHandler.info("Converting old message index: messages.enabled");
        }

        if(contains("messages.disabled"))
        {
            MessageConfig.self.set(ConfigMessageLookup.messageDisabledMessage(), getString("messages.disabled.message"));
            MessageConfig.self.set(ConfigMessageLookup.messageDisabledColor(), getList("messages.disabled.color"));
            LoggingHandler.info("Converting old message index: messages.disabled");
        }

        if(contains("messages.set"))
        {
            MessageConfig.self.set(ConfigMessageLookup.messageSetMessage(), getString("messages.set.message"));
            MessageConfig.self.set(ConfigMessageLookup.messageSetColor(), getList("messages.set.color"));
            LoggingHandler.info("Converting old message index: messages.set");
        }

        if (contains("messages.sleeping")) {
            MessageConfig.self.set(ConfigMessageLookup.messageSleepingColor(), getList("messages.sleeping.color"));
            MessageConfig.self.set(ConfigMessageLookup.messageSleepingMessage(), getString("messages.sleeping.message"));
            LoggingHandler.info("Converting old message index: messages.sleeping");
        }

        if (contains("messages.not_sleeping")) {
            MessageConfig.self.set(ConfigMessageLookup.messageNotSleepingColor(), getList("messages.not_sleeping.color"));
            MessageConfig.self.set(ConfigMessageLookup.messageNotSleepingMessage(), getString("messages.not_sleeping.message"));
            LoggingHandler.info("Converting old message index: messages.sleeping");
        }

        if(contains("messages.total"))
        {
            MessageConfig.self.set(ConfigMessageLookup.messageTotalMessage(), getString("messages.total.message"));
            MessageConfig.self.set(ConfigMessageLookup.messageTotalColor(), getList("messages.total.color"));
            LoggingHandler.info("Converting old message index: messages.total");
        }

        if(contains("messages.daytick"))
        {
            MessageConfig.self.set(ConfigMessageLookup.messageDayTickMessage(), getString("messages.daytick.message"));
            MessageConfig.self.set(ConfigMessageLookup.messageDayTickColor(), getList("messages.daytick.color"));
            LoggingHandler.info("Converting old message index: messages.daytick");
        }

        if(contains("messages.worldDisable"))
        {
            MessageConfig.self.set(ConfigMessageLookup.messageWorldDisableMessage(), getString("messages.worldDisable.message"));
            MessageConfig.self.set(ConfigMessageLookup.messageWorldDisableColor(), getList("messages.worldDisable.color"));
            LoggingHandler.info("Converting old message index: messages.worldDisable");
        }

        if(contains("messages.worldDisableFailed"))
        {
            MessageConfig.self.set(ConfigMessageLookup.messageWorldDisableFailedMessage(), getString("messages.worldDisableFailed.message"));
            MessageConfig.self.set(ConfigMessageLookup.messageWorldDisableFailedColor(), getList("messages.worldDisableFailed.color"));
            LoggingHandler.info("Converting old message index: messages.worldDisableFailed");
        }

        set("messages", null);

        save();
        MessageConfig.self.save();
    }

    @Override
    protected String getFileName() {
        return "config";
    }
}
