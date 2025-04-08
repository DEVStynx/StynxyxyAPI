package de.stynxyxy.stynxyxyAPI;

import de.stynxyxy.stynxyxyAPI.command.APICommand;
import de.stynxyxy.stynxyxyAPI.command.APIRegistry;
import de.stynxyxy.stynxyxyAPI.config.custom.DatabaseConfiguration;
import de.stynxyxy.stynxyxyAPI.config.custom.MainConfig;
import de.stynxyxy.stynxyxyAPI.config.PluginConfig;
import de.stynxyxy.stynxyxyAPI.database.sql.Repository;
import de.stynxyxy.stynxyxyAPI.database.sql.SQLDatabaseConnectorService;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;


public class PaperAPI extends BaseAPI{
    private static JavaPlugin plugin;
    public static String prefix = "&7[&aStynxyxyAPI&7] &f";
    private static Map<String, PluginConfig> APIconfigurations;
    private static Map<String, Repository<?,?>> APIRepositories;



    private static APIRegistry commandRegistry;
    private static SQLDatabaseConnectorService sqlDatabaseConnectorService;


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




        prefix = mainConfig.getFormatted("prefix");
        BaseAPI.setLanguage(mainConfig.getConfig().getString("language"));
        BaseAPI.setUsingDatabase(mainConfig.getConfig().getBoolean("database"));


        if (getUsingDatabase()) {



            APIRepositories = new HashMap<>();
            DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();
            PaperAPI.registerConfig(databaseConfiguration);

           sqlDatabaseConnectorService  = new SQLDatabaseConnectorService(
                    databaseConfiguration.getConfig().getString("url"),
                    databaseConfiguration.getConfig().getString("username"),
                    databaseConfiguration.getConfig().getString("password")
                    );

        }

        BaseAPI.APIlogger.info("Enabled " + prefix);
    }



    public static JavaPlugin getCustomPlugin() {
        return plugin;
    }
    public static <ENTITY,ID> Repository<ENTITY,ID> addRepository(Class<ENTITY> entityClass, Class<ID> idClass, String identifier) {
        if (!getUsingDatabase()) {
            BaseAPI.APIlogger.warning("You are trying to add a database repository, enable Database Access first.");
            return null;
        }
        if (APIRepositories.containsKey(identifier)) {
            BaseAPI.APIlogger.warning("You are trying to add a repository with a duplicate key!");
            return null;
        }

        Repository<ENTITY,ID> repository = sqlDatabaseConnectorService.openRepository(entityClass, idClass);
        APIRepositories.put(identifier, repository);
        return repository;
    }

    public static void registerCommand(APICommand command) {
        commandRegistry.registerCommand(command);
    }
    public static void connectDatabase() {
        if (!getUsingDatabase()) {
            BaseAPI.APIlogger.warning("You are trying to Connect to a repository with disabling database support!");
            return;
        }
        sqlDatabaseConnectorService.connect();
    }


    public static void registerConfig(PluginConfig config) {
        APIconfigurations.put(config.getFile().getName().replace(".yml",""),config);
    }

    public static PluginConfig getConfig(String name) {
        return Optional.ofNullable(APIconfigurations.get(name)).orElseThrow(() -> new IllegalStateException("didn't find config!"));
    }

}
