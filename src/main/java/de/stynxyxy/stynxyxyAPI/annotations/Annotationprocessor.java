package de.stynxyxy.stynxyxyAPI.annotations;

import de.stynxyxy.stynxyxyAPI.BaseAPI;
import de.stynxyxy.stynxyxyAPI.PaperAPI;
import de.stynxyxy.stynxyxyAPI.annotations.config.AutoRegisterConfig;
import de.stynxyxy.stynxyxyAPI.annotations.db.AutoRegisterEntity;
import de.stynxyxy.stynxyxyAPI.command.APICommand;
import de.stynxyxy.stynxyxyAPI.config.PluginConfig;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.bukkit.event.Listener;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Annotationprocessor {
    private static Reflections reflections;
    private static Set<Class<?>> listeners;
    /**
     * The queue of Repositories to automatically register;
     */
    private static Map<Class<?>,Class<?>> repositoriesTodo;

    static {
        if (PaperAPI.getCustomPlugin() == null) {
            throw new RuntimeException("The Plugin doesn't exist yet!");
        }
        reflections = new Reflections(PaperAPI.getCustomPlugin().getClass().getPackageName());

        listeners = new HashSet<>();
        repositoriesTodo = new HashMap<>();

    }

    /**
     * A Simple Way to find all {@link PluginConfig Configurations} with the {@link AutoRegisterConfig} Annotation
     * @return A {@link Set<PluginConfig>} of PluginConfigs
     */
    public static Set<Class<? extends PluginConfig>> findAutoRegisteredConfigs() {
        Set<Class<? extends PluginConfig>> configclasses = new HashSet<>();
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(AutoRegisterConfig.class);
        for (Class<?> clazz : classes) {
            BaseAPI.APIlogger.info("Found Class with Annotation: "+clazz.getSimpleName());
            if (PluginConfig.class.isAssignableFrom(clazz)) {
                BaseAPI.APIlogger.info("Did Pass");
                Class<? extends PluginConfig> configClass = (Class<? extends PluginConfig>) clazz;
                configclasses.add(configClass);
                BaseAPI.APIlogger.info("☑️Automatically Registered ConfigClass: "+configClass.getSimpleName());
            }
        }
        return configclasses;
    }

    /**
     * A Simple Way to find all {@link APICommand Configurations} with the {@link AutoRegister} Annotation
     * @return A {@link Set<APICommand>} of APICommands
     * @apiNote This works for {@link org.bukkit.command.Command SpigotCommands} and {@link APICommand APICommands} only!
     */
    public static Set<Class<? extends APICommand>> findRegisteredCommands() {
        listeners.clear();
        Set<Class<? extends APICommand>> commandClasses = new HashSet<>();
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(AutoRegister.class);
        for (Class<?> clazz: classes) {
            if (APICommand.class.isAssignableFrom(clazz)) {
                Class<? extends APICommand> commandClass = (Class<? extends APICommand>) clazz;
                commandClasses.add(commandClass);
            }
            if (Listener.class.isAssignableFrom(clazz)) {
                Class<?> listener = clazz;
                listeners.add(listener);
            }
        }

        return commandClasses;

    }

    /**
     * A Simple Way to find all Classes with the {@link Entity} and the {@link AutoRegisterEntity} Annotation
     * @return A {@link Set} of Classes
     * @apiNote There can also be Classes without the {@link Entity EntityAnnotation} if the Attribute force {@link Boolean true}
     */
    public static Set<Class<?>> findEntites() {
        Set<Class<?>> entityClasses = new HashSet<>();
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(AutoRegisterEntity.class);
        for (Class<?> clazz: classes) {
            AutoRegisterEntity annotation = clazz.getAnnotation(AutoRegisterEntity.class);
            if (clazz.isAnnotationPresent(Entity.class) || annotation.force()) {
                entityClasses.add(clazz);
                BaseAPI.APIlogger.info("☑️Automatically Registered Entity: "+clazz.getSimpleName());
            }
            if (annotation.autorepository()) {
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Id.class)) {
                        repositoriesTodo.put(clazz,field.getType());
                    }
                }
            }
        }
        return entityClasses;
    }

    /**
     * A Simple way to find all {@link Listener Listeners}
     * @return A {@link Set} of Listeners
     * @apiNote Do only use this Method if findRegisteredCommands was already called
     * The findRegisteredCommands Method will be automatically called on the Plugin start
     */
    public static Set<Class<?>> getListeners() {
        return listeners;
    }
    /**
     * A Simple way to find all {@link Class Entites} to create a Repository for
     * @return A {@link Map} of Classes
     * @apiNote Do only use this Method if findEntites was already called
     * The findEntites Method will be automatically called on the Plugin start if the Database Connection is {@link Boolean true}
     */
    public static Map<Class<?>,Class<?>> getRepositoriesTodo() {
        return repositoriesTodo;
    }
}
