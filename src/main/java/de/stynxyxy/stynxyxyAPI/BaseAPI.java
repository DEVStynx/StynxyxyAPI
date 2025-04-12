package de.stynxyxy.stynxyxyAPI;

import lombok.Getter;
import lombok.Setter;


import java.util.logging.Logger;

public class BaseAPI {

    private static BaseAPI INSTANCE;
    public static Logger APIlogger;
    public static String APILabel = "StynxyxyAPI";
    @Setter
    @Getter
    private static String language;
    protected static boolean requiresDatabase = false;
    @Setter
    @Getter
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
    public static boolean getUsingDatabase() {
        return usingDatabase;
    }
    public static void requireDatabase() {requiresDatabase = true;}

}
