package de.stynxyxy.stynxyxyAPI.config.custom;

import de.stynxyxy.stynxyxyAPI.config.PluginConfig;

public class DatabaseConfiguration extends PluginConfig {
    public DatabaseConfiguration() {
        super("database.yml");

    }

    @Override
    public void getdefaults() {
        addDefault("url","");
        addDefault("username","");
        addDefault("password","");
    }
}
