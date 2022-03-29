package de.leximon.api.command;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.function.Predicate;

public enum CommandUser {

	CONSOLE(sender -> sender instanceof ConsoleCommandSender),
	COMMAND_BLOCK(sender -> sender instanceof BlockCommandSender),
	PLAYER(sender -> sender instanceof Player),
	ALL(sender -> true);

	protected final Predicate<CommandSender> predicate;

	CommandUser(Predicate<CommandSender> predicate) {
		this.predicate = predicate;
	}

}