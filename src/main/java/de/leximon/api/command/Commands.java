package de.leximon.api.command;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandDispatcher;
import net.minecraft.commands.CommandListenerWrapper;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Commands {

	public static int SINGLE_SUCCESS = 1;

	public ArgumentBuilder<CommandListenerWrapper, ?> b;
	protected String name;

	public static Commands literal(String name) {
		Commands c = new Commands();
		c.b = CommandDispatcher.a(name);
		c.name = name;
		return c;
	}

	/*public static Commands literal(String name, BiConsumer<CommandSender, CommandContext<CommandListenerWrapper>> pre) {
		Commands c = new Commands();
		c.b = CommandDispatcher.a(name);
		c.b.executes(context -> {
			pre.accept(context.getSource().getBukkitSender(), context);
			return 1;
		});
		return c;
	}*/

	public static Commands argument(String name, ArgumentType<?> type) {
		Commands c = new Commands();
		c.b = CommandDispatcher.a(name, type);
		return c;
	}

	public Commands then(Commands command) {
		b.then(command.b);
		return this;
	}

	public Commands redirect(Commands command) {
		b.redirect(command.b.build());
		return this;
	}

	@SuppressWarnings("unchecked")
	public Commands suggests(SuggestionProvider<?> provider) {
		if (b instanceof RequiredArgumentBuilder) {
			RequiredArgumentBuilder<CommandListenerWrapper, ?> rab = (RequiredArgumentBuilder<CommandListenerWrapper, ?>) b;
			rab.suggests((SuggestionProvider<CommandListenerWrapper>) provider);
		} else {
			throw new IllegalArgumentException("Unable to add a list to a literal argument");
		}
		return this;
	}

	public Commands executes(Command command) {
		b.executes(context -> command.run(context.getSource().getBukkitSender(), context));
		return this;
	}
	private final List<String> perms = new ArrayList<>();
	private final List<CommandUser> users = new ArrayList<>();

	public Commands perm(String[] permissions) {
		perms.addAll(Arrays.asList(permissions));
		return this;
	}

	public Commands perm(String permission) {
		perms.add(permission);
		return this;
	}

	public Commands user(CommandUser... users) {
		this.users.addAll(Arrays.asList(users));
		return this;
	}

	private Predicate<CommandSender> predicate = s -> true;

	public Commands requires(Predicate<CommandSender> predicate) {
		this.predicate = predicate;
		return this;
	}

	public boolean hasPermission(CommandSender sender) {
		if(sender.isOp())
			return true;
		for (String perm : perms)
			if (!sender.hasPermission(perm))
				return false;

		for (CommandUser user : users)
			if(user.predicate.and(this.predicate).test(sender))
				return true;
		return false;
	}

	protected void updateRequirements() {
		b.requires(cc -> hasPermission(cc.getBukkitSender()));
	}

	public static SuggestionProvider<?> list(Consumer<Suggestions> list) {
		return (context, builder) -> {
			String input = builder.getInput().substring(builder.getStart());
			Suggestions suggestions = new Suggestions(context);
			list.accept(suggestions);
			for(Entry<String, String> entry : suggestions.texts.entrySet()) {
				if(entry.getKey().startsWith(input)) {
					if(entry.getValue() == null)
						builder.suggest(entry.getKey());
					else
						builder.suggest(entry.getKey(), new LiteralMessage(entry.getValue()));
				}
			}
			return builder.buildFuture();
		};
	}

	public static class Suggestions {
		private final HashMap<String, String> texts = new HashMap<String, String>();
		private final CommandContext<?> context;

		public Suggestions(CommandContext<?> context) {
			this.context = context;
		}

		public void add(String text, String tooltip) {
			texts.put(text, tooltip);
		}

		public void add(String text) {
			add(text, null);
		}

		public CommandContext<?> getContext() {
			return context;
		}
	}
}

