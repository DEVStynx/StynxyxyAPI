package de.stynxyxy.stynxyxyAPI.config.custom;

import de.stynxyxy.stynxyxyAPI.BaseAPI;
import de.stynxyxy.stynxyxyAPI.PaperAPI;
import de.stynxyxy.stynxyxyAPI.annotations.config.AutoRegisterConfig;
import de.stynxyxy.stynxyxyAPI.config.PluginConfig;

@AutoRegisterConfig
public class MainConfig extends PluginConfig {

    public MainConfig() {
        super("config.yml");
    }

    @Override
    public void getdefaults() {
        addDefault("language","en");
        addDefault("prefix","&7[&aStynxyxyAPI&7] &f");
        addDefault("database",true);
    }
}
