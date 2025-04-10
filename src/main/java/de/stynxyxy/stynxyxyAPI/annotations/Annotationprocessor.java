package de.stynxyxy.stynxyxyAPI.annotations;

import de.stynxyxy.stynxyxyAPI.BaseAPI;
import de.stynxyxy.stynxyxyAPI.PaperAPI;
import de.stynxyxy.stynxyxyAPI.annotations.config.AutoRegister;
import de.stynxyxy.stynxyxyAPI.annotations.config.AutoRegisterConfig;
import de.stynxyxy.stynxyxyAPI.annotations.config.AutoRegisterEntity;
import de.stynxyxy.stynxyxyAPI.command.APICommand;
import de.stynxyxy.stynxyxyAPI.config.PluginConfig;
import jakarta.persistence.Entity;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Annotationprocessor {
    private static Reflections reflections;

    static {
        if (PaperAPI.getCustomPlugin() == null) {
            throw new RuntimeException("The Plugin doesn't exist yet!");
        }
        reflections = new Reflections(PaperAPI.getCustomPlugin().getClass().getPackageName());
    }

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

    public static Set<Class<? extends APICommand>> findRegisteredCommands() {
        Set<Class<? extends APICommand>> commandClasses = new HashSet<>();
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(AutoRegister.class);
        for (Class<?> clazz: classes) {
            if (clazz.isAssignableFrom(APICommand.class)) {
                Class<? extends APICommand> commandClass = (Class<? extends APICommand>) clazz;
                commandClasses.add(commandClass);
                BaseAPI.APIlogger.info("☑️Automatically Registered Command: "+clazz.getSimpleName());
            }
        }

        return commandClasses;

    }

    public static Set<Class<?>> findEntites() {
        Set<Class<?>> entityClasses = new HashSet<>();
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(AutoRegisterEntity.class);
        for (Class<?> clazz: classes) {
            if (clazz.isAnnotationPresent(Entity.class)) {
                entityClasses.add(clazz);
                BaseAPI.APIlogger.info("☑️Automatically Registered Entity: "+clazz.getSimpleName());
            }
        }
        return entityClasses;
    }
}
