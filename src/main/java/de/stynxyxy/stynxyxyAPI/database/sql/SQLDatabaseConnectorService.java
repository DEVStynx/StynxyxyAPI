package de.stynxyxy.stynxyxyAPI.database.sql;

import de.stynxyxy.stynxyxyAPI.BaseAPI;
import de.stynxyxy.stynxyxyAPI.database.sql.config.SQLConfiguration;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SQLDatabaseConnectorService {
    private SQLConfiguration configuration;
    private static SessionFactory sessionFactory;

    private static Set<Class<?>> databaseRelevantClasses;

    public SQLDatabaseConnectorService(String url, String username, String password) {
        configuration = new SQLConfiguration(url,username,password);
        databaseRelevantClasses = new HashSet<>();

    }
    public void connect() {
        for (Class<?> clazz : databaseRelevantClasses) {
            configuration.getConfig().addAnnotatedClass(clazz);
            System.out.println("Added Database relevant class!");
        }
        sessionFactory = configuration.getConfig().buildSessionFactory();
    }


    public static void addDatabaseRelevantClass(Class<?> clazz) {
        databaseRelevantClasses.add(clazz);
    }

    public static Optional<SessionFactory> getSessionFactory() {
        if (BaseAPI.getUsingDatabase()) {
            return Optional.of(sessionFactory);
        } else {
            BaseAPI.APIlogger.info("The Database is disabled, if you aren't a Developer, ignore it");
        }
        return Optional.empty();
    }

    public <ENTITY,ID> Repository<ENTITY,ID> openRepository(Class<ENTITY> entityClass, Class<ID> id) {
        return new Repository<>(sessionFactory,entityClass,id);
    }

}
