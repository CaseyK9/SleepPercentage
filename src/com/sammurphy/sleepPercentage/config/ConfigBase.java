package com.sammurphy.sleepPercentage.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public abstract class ConfigBase extends YamlConfiguration {

    protected final File fileLocation;
    protected final JavaPlugin plugin;

    public ConfigBase(JavaPlugin plugin) {
        this.plugin = plugin;

        fileLocation = new File(plugin.getDataFolder() + "/" +  getFileName() + ".yml");

        try {
            load(fileLocation);
        } catch (FileNotFoundException e) {

        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        setupDefaultFile();

        options().copyDefaults(true);
        options().copyHeader(true);

        save();
    }

    protected abstract String getFileName();

    private void setupDefaultFile()
    {
        Reader defConfigStream = null;

        try {
            defConfigStream = new InputStreamReader(plugin.getResource(getFileName() + ".yml"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        assert defConfigStream != null;
        setDefaults(YamlConfiguration.loadConfiguration(defConfigStream));
    }

    public void save()
    {
        try {
            save(fileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
