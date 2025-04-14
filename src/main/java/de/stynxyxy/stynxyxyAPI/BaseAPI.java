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
    private static boolean usingDatabase;

    /**
     * The manual way to enable the {@link BaseAPI}
     * @param api
     */
    public static void setAPI(BaseAPI api) {
        INSTANCE = api;
        usingDatabase = true;
    }

    /**
     * The way to Get the enabled API as a {@link BaseAPI}
     * @return the {@link BaseAPI}
     */
    public static BaseAPI get() {
        return INSTANCE;
    }

    protected static void setAPIlogger(Logger logger) {
        APIlogger = logger;
    }

    /**
     * Get if the Database is manually disabled
     * @return the {@link Boolean} if the Database Connection is disabled
     */
    public static boolean getUsingDatabase() {
        return usingDatabase;
    }
    public static void requireDatabase() {requiresDatabase = true;}

}
