package de.stynxyxy.stynxyxyAPI;

import de.stynxyxy.stynxyxyAPI.command.APICommand;
import de.stynxyxy.stynxyxyAPI.command.APIRegistry;
import de.stynxyxy.stynxyxyAPI.config.custom.DatabaseConfiguration;
import de.stynxyxy.stynxyxyAPI.config.custom.MainConfig;
import de.stynxyxy.stynxyxyAPI.config.PluginConfig;
import de.stynxyxy.stynxyxyAPI.menu.Menu;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;


public class PaperAPI extends BaseAPI{
    private static JavaPlugin plugin;
    public static String prefix = "&7[&aStynxyxyAPI&7] &f";
    private static Map<String, PluginConfig> APIconfigurations;

    private static APIRegistry commandRegistry;


    private PaperAPI(JavaPlugin sPlugin) {
        plugin = sPlugin;
        commandRegistry = new APIRegistry();
    }

    public static void enableAPI(JavaPlugin sPlugin) {
        setAPI(new PaperAPI(sPlugin));
        setAPIlogger(plugin.getLogger());

        APIconfigurations = new HashMap<>();


        MainConfig mainConfig = new MainConfig();
        PaperAPI.registerConfig(mainConfig);

        DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();
        PaperAPI.registerConfig(databaseConfiguration);


        prefix = mainConfig.getFormatted("prefix");
        BaseAPI.setLanguage(mainConfig.getConfig().getString("language"));
        BaseAPI.setUsingDatabase(mainConfig.getConfig().getBoolean("database"));

        BaseAPI.APIlogger.info("Enabled " + prefix);
    }


    public static JavaPlugin getCustomPlugin() {
        return plugin;
    }

    public static void registerCommand(APICommand command) {
        commandRegistry.registerCommand(command);
    }

    public static void registerConfig(PluginConfig config) {
        APIconfigurations.put(config.getFile().getName().replace(".yml",""),config);
    }

    public static PluginConfig getConfig(String name) {
        return Optional.ofNullable(APIconfigurations.get(name)).orElseThrow(() -> new IllegalStateException("didn't find config!"));
    }

}
