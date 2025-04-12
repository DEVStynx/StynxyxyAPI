package de.stynxyxy.stynxyxyAPI.database.sql;

import de.stynxyxy.stynxyxyAPI.BaseAPI;
import de.stynxyxy.stynxyxyAPI.annotations.Annotationprocessor;
import de.stynxyxy.stynxyxyAPI.database.BaseRepository;
import de.stynxyxy.stynxyxyAPI.database.DatabaseService;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Arrays;
import java.util.List;

public class SQLDatabaseService extends DatabaseService {
    /**
     * @param url      the url for the Database jdbc conenction loaded by the database-config
     * @param username the username for the Database jdbc conenction loaded by the database-config
     * @param password the password for the Database jdbc conenction loaded by the database-config
     */
    private SessionFactory sessionFactory;
    private List<Class<?>> entityList;

    public SQLDatabaseService(String url, String username, String password,Class<?>... dbentities) {
        super(url, username, password);
        entityList = Arrays.stream(dbentities).toList();
        sessionFactory = getHibernateConfiguration(url,username,password,"com.mysql.cj.jdbc.Driver").buildSessionFactory();
        BaseAPI.APIlogger.info("Constructed Sessionfactory: "+(sessionFactory));
    }

    public Configuration getHibernateConfiguration(String url, String username, String password,String driver) {

        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.url", url);
        configuration.setProperty("hibernate.connection.username", username);
        configuration.setProperty("hibernate.connection.password", password);

        configuration.setProperty("hibernate.connection.driver_class", driver);
        for (Class<?> passedClass: entityList) {
            configuration.addAnnotatedClass(passedClass);
        }
        for (Class<?> loadedClass : Annotationprocessor.findEntites()) {
            BaseAPI.APIlogger.info("Automatically "+loadedClass.getName()+" to Hibernate Config!");
            configuration.addAnnotatedClass(loadedClass);
        }
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.format_sql", "true");

        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.setProperty("spring.jpa.hibernate.ddl-auto", "auto");
        return configuration;
    }

    @Override
    protected <ENTITY, ID> BaseRepository<ENTITY, ID> createRepositoryspecific(Class<ENTITY> entityClass, Class<ID> idClass) {
        return new SQLRepository<ENTITY,ID>(entityClass,idClass,sessionFactory);
    }
}
