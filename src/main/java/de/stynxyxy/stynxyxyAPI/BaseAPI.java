package de.stynxyxy.stynxyxyAPI;

import org.bukkit.plugin.java.JavaPlugin;


import java.util.logging.Logger;

public class BaseAPI {

    private static BaseAPI INSTANCE;
    public static Logger APIlogger;
    public static String APILabel = "StynxyxyAPI";
    private static String language;

    private static boolean usingDatabase;


    public static void setAPI(BaseAPI api) {
        INSTANCE = api;
        usingDatabase = true;
    }
    public static BaseAPI get() {
        return INSTANCE;
    }
    protected  static void setAPIlogger(Logger logger) {
        APIlogger = logger;
    }
    public static void setLanguage(String planguage) {
        language = planguage;
    }
    public static void setUsingDatabase(boolean database) {
        usingDatabase = database;
    }

}
