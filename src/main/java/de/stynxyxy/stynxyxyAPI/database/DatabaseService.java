package de.stynxyxy.stynxyxyAPI.database;

import de.stynxyxy.stynxyxyAPI.BaseAPI;
import de.stynxyxy.stynxyxyAPI.PaperAPI;
import de.stynxyxy.stynxyxyAPI.config.PluginConfig;
import de.stynxyxy.stynxyxyAPI.database.sql.SQLDatabaseService;

import java.util.HashMap;
import java.util.Map;

public abstract class DatabaseService {
    Map<Class<?>, BaseRepository<?,?>> loadedEntites;

    /**
     *
     * @param url the url for the Database jdbc conenction loaded by the database-config
     * @param username the username for the Database jdbc conenction loaded by the database-config
     * @param password the password for the Database jdbc conenction loaded by the database-config
     */
    public DatabaseService(String url, String username, String password) {
        loadedEntites = new HashMap<>();
    }

    protected  abstract<ENTITY, ID> BaseRepository<ENTITY,ID> createRepositoryspecific(Class<ENTITY> entityClass, Class<ID> idClass);


    public <ENTITY, ID> BaseRepository<ENTITY,ID> createRepository(Class<ENTITY> entityClass, Class<ID> idClass) {
        BaseAPI.APIlogger.info("creating Repository for: "+entityClass.getName() +" with id: "+idClass.getName());
        BaseRepository<ENTITY,ID> repo = createRepositoryspecific(entityClass, idClass);
        loadedEntites.put(entityClass,repo);
        return repo;
    }
    public BaseRepository<?,?> getRepository(Class<?> entityClass) {
        return loadedEntites.get(entityClass);
    }
    public static DatabaseService loadService() {
        try {
            PluginConfig config = PaperAPI.getConfig("database.yml");
            String url = config.getConfig().getString("url");
            String username = config.getConfig().getString("username");
            String password = config.getConfig().getString("password");

            BaseAPI.APIlogger.info("Constructed SQLDatabaseService:");
            BaseAPI.APIlogger.info("URL: "+url);
            BaseAPI.APIlogger.info("USERNAME: "+username);
            BaseAPI.APIlogger.info("PASSWORD: "+password);
            DatabaseService databaseService = new SQLDatabaseService(url,username,password);
            return databaseService;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
