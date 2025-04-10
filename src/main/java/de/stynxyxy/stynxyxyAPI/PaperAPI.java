package de.stynxyxy.stynxyxyAPI;

import de.stynxyxy.stynxyxyAPI.annotations.Annotationprocessor;
import de.stynxyxy.stynxyxyAPI.command.APICommand;
import de.stynxyxy.stynxyxyAPI.command.APIRegistry;
import de.stynxyxy.stynxyxyAPI.config.custom.DatabaseConfiguration;
import de.stynxyxy.stynxyxyAPI.config.custom.MainConfig;
import de.stynxyxy.stynxyxyAPI.config.PluginConfig;
import de.stynxyxy.stynxyxyAPI.database.DatabaseService;
import de.stynxyxy.stynxyxyAPI.database.sql.SQLDatabaseService;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


public class PaperAPI extends BaseAPI{
    private static JavaPlugin plugin;
    public static String prefix = "&7[&aStynxyxyAPI&7] &f";
    private static Map<String, PluginConfig> APIconfigurations;
    private static DatabaseService databaseService;

    private static APIRegistry commandRegistry;



    private PaperAPI(JavaPlugin sPlugin) {
        plugin = sPlugin;
        commandRegistry = new APIRegistry();
    }
    public static void enableAPI(JavaPlugin sPlugin, Class<?>... dbClasses) {
        setAPI(new PaperAPI(sPlugin));
        setAPIlogger(plugin.getLogger());

        APIconfigurations = new HashMap<>();


        MainConfig mainConfig = new MainConfig();
        PaperAPI.registerConfig(mainConfig);

        prefix = mainConfig.getFormatted("prefix");
        BaseAPI.setLanguage(mainConfig.getConfig().getString("language"));
        BaseAPI.setUsingDatabase(mainConfig.getConfig().getBoolean("database"));

        try {
            registerClassesAutomatically();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (getUsingDatabase()) {
            APIlogger.info("Started setting up db!");


            DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();
            PaperAPI.registerConfig(databaseConfiguration);
            BaseAPI.APIlogger.info("Database information from database.yml");
            BaseAPI.APIlogger.info("URL: "+databaseConfiguration.getConfig().getString("url"));
            BaseAPI.APIlogger.info("USERNAME: "+databaseConfiguration.getConfig().getString("username"));
            BaseAPI.APIlogger.info("PASSWORD: "+databaseConfiguration.getConfig().getString("password"));
            databaseService = new SQLDatabaseService(databaseConfiguration.getConfig().getString("url"),
                    databaseConfiguration.getConfig().getString("username"),
                    databaseConfiguration.getConfig().getString("password"),
                    dbClasses
            );
        }

        BaseAPI.APIlogger.info("Enabled " + prefix);
    }

    public static void enableAPI(JavaPlugin sPlugin) {
        enableAPI(sPlugin, new Class[0]);
    }



    public static JavaPlugin getCustomPlugin() {
        return plugin;
    }


    public static void registerCommand(APICommand command) {
        commandRegistry.registerCommand(command);
    }
    public static void connectDatabase() {
        if (!getUsingDatabase()) {
            BaseAPI.APIlogger.warning("You are trying to Connect to a repository with disabling database support!");
        }

    }
    private static void registerClassesAutomatically() throws Exception {
        //Config Autoregister
        Set<Class<? extends PluginConfig>> configs = Annotationprocessor.findAutoRegisteredConfigs();
        for (Class<? extends PluginConfig> config : configs) {
            //Find first valid constructor
            Constructor<?>[] constructors = config.getDeclaredConstructors();
            for (Constructor<?> constructor: constructors) {
                if (constructor.getParameterCount() == 0 ) {
                    BaseAPI.APIlogger.info("construction way 1");
                    PluginConfig configInstance = (PluginConfig) constructor.newInstance();
                    configInstance.save();
                    APIconfigurations.put(configInstance.getClass().getSimpleName() + ".yml",configInstance);
                } else if(constructor.getParameterCount() == 1 && constructor.getParameters()[0].getType() == String.class) {
                    PluginConfig configInstance = (PluginConfig) constructor.newInstance(config.getSimpleName() + ".yml");
                    configInstance.save();
                    APIconfigurations.put(configInstance.getClass().getSimpleName(),configInstance);
                }
            }

        }
    }


    public static void registerConfig(PluginConfig config) {
        APIconfigurations.put(config.getFile().getName().replace(".yml",""),config);
    }

    public static PluginConfig getConfig(String name) {
        return Optional.ofNullable(APIconfigurations.get(name)).orElseThrow(() -> new IllegalStateException("didn't find config!"));
    }
    public static DatabaseService getDatabaseService() {
        return databaseService;
    }

}
