package com.sammurphy.sleepPercentage.config;

import org.bukkit.plugin.java.JavaPlugin;

public class MessageConfig extends ConfigBase {

    public static MessageConfig self;

    public MessageConfig(JavaPlugin plugin) {
        super(plugin);
        self = this;
    }

    @Override
    protected String getFileName() {
        return "messages";
    }
}
