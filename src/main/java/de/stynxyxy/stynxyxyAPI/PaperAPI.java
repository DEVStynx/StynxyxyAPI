package de.stynxyxy.stynxyxyAPI;

import de.stynxyxy.stynxyxyAPI.annotations.Annotationprocessor;
import de.stynxyxy.stynxyxyAPI.annotations.AutoRegister;
import de.stynxyxy.stynxyxyAPI.command.APICommand;
import de.stynxyxy.stynxyxyAPI.command.APIRegistry;
import de.stynxyxy.stynxyxyAPI.config.custom.DatabaseConfiguration;
import de.stynxyxy.stynxyxyAPI.config.custom.MainConfig;
import de.stynxyxy.stynxyxyAPI.config.PluginConfig;
import de.stynxyxy.stynxyxyAPI.database.DatabaseService;
import de.stynxyxy.stynxyxyAPI.database.sql.SQLDatabaseService;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
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
            try {
                databaseService = new SQLDatabaseService(databaseConfiguration.getConfig().getString("url"),
                        databaseConfiguration.getConfig().getString("username"),
                        databaseConfiguration.getConfig().getString("password"),
                        dbClasses
                );
                for (Class<?> newRepo : Annotationprocessor.getRepositoriesTodo().keySet()) {
                    PaperAPI.getDatabaseService().createRepository(newRepo,Annotationprocessor.getRepositoriesTodo().get(newRepo));
                    BaseAPI.APIlogger.info("☑️Created Repository Automatically for Entity: "+newRepo.getSimpleName()+ "and id: "+Annotationprocessor.getRepositoriesTodo().get(newRepo).getName());
                }
            } catch (Exception e) {
                BaseAPI.APIlogger.info("Couldn't connect to the database: ");
                BaseAPI.APIlogger.warning(e.getMessage());
                e.printStackTrace();
                if (requiresDatabase) {
                    BaseAPI.APIlogger.warning("Disabled Plugin");
                    getCustomPlugin().setEnabled(false);
                }
            }
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
        BaseAPI.APIlogger.info("☑️Registered Command "+command.getName());
    }

    private static void registerClassesAutomatically() throws Exception {
        //Config Autoregister
        Set<Class<? extends PluginConfig>> configs = Annotationprocessor.findAutoRegisteredConfigs();
        for (Class<? extends PluginConfig> config : configs) {
            //Find first valid constructor
            Constructor<?>[] constructors = config.getDeclaredConstructors();
            for (Constructor<?> constructor: constructors) {
                if (constructor.getParameterCount() == 0 ) {
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

        //Command Autoregister
        Set<Class<? extends APICommand>> commands = Annotationprocessor.findRegisteredCommands();
        boolean registeredCurrent = false;
        for (Class<? extends APICommand> commandclass : commands) {
            registeredCurrent = false;
            //Find Valid constructor
            Constructor<?>[] constructors = commandclass.getDeclaredConstructors();
            for (Constructor<?> constructor : constructors) {
                if (constructor.getParameterCount() == 0) {
                    APICommand commandInstance = (APICommand) constructor.newInstance();
                    registerCommand(commandInstance);
                    registeredCurrent = true;
                    continue;
                } else if(constructor.getParameterCount() == 1 && constructor.getParameters()[0].getType() == String.class) {
                    AutoRegister annotation = (AutoRegister) commandclass.getAnnotation(AutoRegister.class);
                    APICommand instance;
                    if (annotation.name().isEmpty()) {
                        instance = (APICommand) constructor.newInstance(commandclass.getSimpleName());
                    } else {
                        instance = (APICommand) constructor.newInstance(annotation.name());
                    }

                    registerCommand(instance);
                    registeredCurrent = true;
                    break;
                }

            }
            if (!registeredCurrent) {
                BaseAPI.APIlogger.info("❌There was an Issue auto registering command: Too many or invalid Parameters: "+commandclass.getSimpleName());
            }
        }

        //Listener Autoregister
        Set<Class<?>> listeners = Annotationprocessor.getListeners();
        for (Class<?> listener : listeners) {
            try {
                for (Constructor<?> constructor : listener.getConstructors()) {
                    if (constructor.getParameterCount() == 0) {
                        Listener listenerinstance = (Listener) constructor.newInstance();
                        getCustomPlugin().getServer().getPluginManager().registerEvents(listenerinstance,getCustomPlugin());
                        break;
                    }
                }
            } catch (Exception e) {
                BaseAPI.APIlogger.warning("There was an Issue registering the Listener: "+listener.getSimpleName());
                BaseAPI.APIlogger.warning(e.getMessage());
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
        if (!getUsingDatabase()) {
            throw new RuntimeException("The Database is disabled");
        }
        return databaseService;
    }
}
