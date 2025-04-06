package de.stynxyxy.stynxyxyAPI.config;

import de.stynxyxy.stynxyxyAPI.BaseAPI;
import de.stynxyxy.stynxyxyAPI.PaperAPI;
import de.stynxyxy.stynxyxyAPI.annotations.config.AutoRegisterConfig;
import de.stynxyxy.stynxyxyAPI.util.MessageUtil;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;



import java.io.File;
import java.io.IOException;

public abstract class PluginConfig {
    @Getter
    private FileConfiguration config;
    @Getter
    private final File file;

    private boolean firstcreated;


    public PluginConfig(String configName) {
        file = new File(PaperAPI.getCustomPlugin().getDataFolder().getAbsolutePath(), configName);

        try {
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            firstcreated = false;
            if (!file.exists()) {
                file.createNewFile();
                firstcreated = true;
            }

            config = YamlConfiguration.loadConfiguration(file);
            if (firstcreated) {
                getdefaults();
                save();
            }

        } catch (IOException e) {
            System.err.println("Fehler beim Erstellen der Datei: " + file.getAbsolutePath());
            throw new RuntimeException(e);
        }


    }

    public void addDefault(String path, Object value) {
        config.set(path,value);
        config.setDefaults(config);
    }
    public abstract void getdefaults();
    public void save() {
        try {
            config.save(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFormatted(String path) {
        return MessageUtil.formatString(getConfig().getString(path));
    }
    public String getFormattedWPrefix(String path) {
        return MessageUtil.formatStringwPrefix(getConfig().getString(path));
    }
}
