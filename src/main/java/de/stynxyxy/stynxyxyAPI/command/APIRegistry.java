package de.stynxyxy.stynxyxyAPI.command;

import de.stynxyxy.stynxyxyAPI.BaseAPI;
import de.stynxyxy.stynxyxyAPI.PaperAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class APIRegistry {
    private HashMap<String, APICommand> loadedCommands;

    public APIRegistry() {
        loadedCommands = new HashMap<>();
    }

    public void registerCommand(APICommand apiCommand) {
        loadedCommands.put(apiCommand.getLabel(),apiCommand);

        CommandMap commandMap = PaperAPI.getCustomPlugin().getServer().getCommandMap();
        commandMap.register(apiCommand.getLabel(),apiCommand);
        commandMap.getKnownCommands().put(apiCommand.getLabel(),apiCommand);

        Bukkit.getOnlinePlayers().forEach(Player::updateCommands);
    }
}
