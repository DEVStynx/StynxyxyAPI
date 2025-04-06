package de.stynxyxy.stynxyxyAPI;


import de.stynxyxy.stynxyxyAPI.command.custom.GuiCommand;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.logging.Logger;

public final class StynxyxyAPI extends JavaPlugin {
    private static Logger logger;

    @Override
    public void onEnable() {

        logger = this.getLogger();
        PaperAPI.enableAPI(this);
        PaperAPI.registerCommand(new GuiCommand());
        getAPILogger().info("Formatted "+PaperAPI.getConfig("config").getFormatted("prefix"));

    }

    @Override
    public void onDisable() {
    }

    public static Logger getAPILogger() {
        return logger;
    }

}
