package de.leximon.api.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface Command {

    int run(CommandSender sender, CommandContext<?> context) throws CommandSyntaxException;

}
