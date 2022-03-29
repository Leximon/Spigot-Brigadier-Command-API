package de.leximon.api.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandListenerWrapper;
import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface Command {

    int run(CommandSender sender, CommandContext<CommandListenerWrapper> context) throws CommandSyntaxException;

}
