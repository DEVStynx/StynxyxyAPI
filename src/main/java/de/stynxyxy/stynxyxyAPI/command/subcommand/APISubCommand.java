package de.stynxyxy.stynxyxyAPI.command.subcommand;

import de.stynxyxy.stynxyxyAPI.command.APICommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class APISubCommand extends APIGroupedCommand {

    public APISubCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    public APISubCommand(@NotNull String name) {
        super(name);
    }

    public APISubCommand(@NotNull String name, String description, @NotNull String permission) {
        super(name, description, permission);
    }

    @Override
    public abstract boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args);

    /**
     *Can be implemented to create new Sub Commands
     *
     */
    @Override
    public void initSubcommands() {

    }
}
