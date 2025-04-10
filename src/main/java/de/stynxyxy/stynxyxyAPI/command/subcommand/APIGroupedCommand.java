package de.stynxyxy.stynxyxyAPI.command.subcommand;

import de.stynxyxy.stynxyxyAPI.command.APICommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class APIGroupedCommand extends APICommand {
    private Set<APISubCommand> subcommands;

    public APIGroupedCommand(@NotNull String name) {
        super(name);
        subcommands = new HashSet<>();
        initSubcommands();

    }


    public APIGroupedCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases);
        subcommands = new HashSet<>();
    }

    public APIGroupedCommand(@NotNull String name, String description, @NotNull String permission) {
        super(name, description, permission);
        subcommands = new HashSet<>();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cBitte gib einen Subcommand an.");
            return false;
        }

        String subLabel = args[0];
        APISubCommand matched = subcommands.stream()
                .filter(cmd -> cmd.getLabel().equalsIgnoreCase(subLabel) || cmd.getAliases().contains(subLabel))
                .findFirst()
                .orElse(null);

        if (matched == null) {
            sender.sendMessage("§cUnbekannter Subcommand: " + subLabel);
            return false;
        }

        // Rekursiv weiterreichen
        String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, subArgs.length);
        return matched.execute(sender, subLabel, subArgs);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> subcommandLabels = new ArrayList<>();
            for (APISubCommand cmd : subcommands) {
                subcommandLabels.add(cmd.getLabel());
                subcommandLabels.addAll(cmd.getAliases());
            }
            return subcommandLabels.stream()
                    .filter(label -> label.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        } else {
            String subLabel = args[0];
            APISubCommand matched = subcommands.stream()
                    .filter(cmd -> cmd.getLabel().equalsIgnoreCase(subLabel) || cmd.getAliases().contains(subLabel))
                    .findFirst()
                    .orElse(null);

            if (matched == null) return List.of();

            String[] subArgs = new String[args.length - 1];
            System.arraycopy(args, 1, subArgs, 0, subArgs.length);
            return matched.tabComplete(sender, subLabel, subArgs);
        }
    }
    public abstract void initSubcommands();


    public void addSubCommand(APISubCommand subCommand) {
        subcommands.add(subCommand);
    }
    public List<APISubCommand> getSupCommands() {
        return subcommands.stream().toList();
    }
}
