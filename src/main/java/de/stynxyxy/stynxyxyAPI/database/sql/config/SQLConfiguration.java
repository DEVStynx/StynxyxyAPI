package de.stynxyxy.stynxyxyAPI.database.sql.config;

import org.hibernate.cfg.Configuration;


public class SQLConfiguration {
    public String databaseURL;
    public String databaseUsername;
    public String databasePassword;

    public SQLConfiguration(String databaseURL, String databaseUsername, String databasePassword) {
        this.databaseURL = databaseURL;
        this.databaseUsername = databaseUsername;
        this.databasePassword = databasePassword;
    }
    public Configuration getConfig() {
        Configuration configuration = new Configuration();

        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.url", databaseURL);
        configuration.setProperty("hibernate.connection.username", databaseUsername);
        configuration.setProperty("hibernate.connection.password", databasePassword);

        configuration.setProperty("hibernate.hbm2ddl.auto", "update"); //Automatically creates tables
        configuration.setProperty("hibernate.format_sql", "true");


        return configuration;
    }
}
