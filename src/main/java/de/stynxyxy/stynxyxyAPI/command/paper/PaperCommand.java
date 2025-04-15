package de.stynxyxy.stynxyxyAPI.command.paper;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Collection;

public abstract class PaperCommand {
    public static int SINGLE_SUCCESS = 1;
    public static int SINGLE_FAILURE = 0;

    private LiteralArgumentBuilder<CommandSourceStack> baseCommand;
    @Getter
    private String description;
    @Getter
    private Collection<String> aliases;
    @Getter
    private String permission;

    public PaperCommand(String literal) {
        baseCommand = Commands.literal(literal);
        initArguements();
        baseCommand.executes(ctx -> {
            if (permission == null || ctx.getSource().getExecutor().hasPermission(permission)) {
                return execute(ctx);
            }
            return SINGLE_FAILURE;
        });
    }
    public PaperCommand(String literal, String description, Collection<String> aliases) {
        this(literal);
        this.description = description;
        this.aliases = aliases;
    }

    public PaperCommand(String literal,String description, Collection<String> aliases, String permission) {

        this(literal,description,aliases);
        this.permission = permission;
    }

    public abstract void initArguements();

    public void addArguement(ArgumentBuilder<CommandSourceStack,?> arguement) {
        baseCommand.then(arguement);
    }
    public void addSubcommand(CommandNode<CommandSourceStack> subcommand) {
        baseCommand.then(subcommand);
    }
    public abstract int execute(CommandContext<CommandSourceStack> ctx);

    protected boolean isPlayerExecutingCommand(CommandContext<CommandSourceStack> ctx) {
        return ctx.getSource().getExecutor() instanceof Player;
    }

    public LiteralCommandNode<CommandSourceStack> build() {
        return baseCommand.build();
    }


}