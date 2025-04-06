package de.stynxyxy.stynxyxyAPI.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class APICommand extends Command {
    protected APICommand(@NotNull String name) {
        super(name);
    }

    protected APICommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases);

    }
    protected APICommand(@NotNull String name, String description, @NotNull String permission) {
        super(name);
        setDescription(description);
        setPermission(permission);
    }

    @Override
    public abstract @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException;
}
