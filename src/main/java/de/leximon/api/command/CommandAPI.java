package de.leximon.api.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandDispatcher;
import net.minecraft.commands.CommandListenerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R2.CraftServer;
import org.bukkit.craftbukkit.v1_19_R2.command.VanillaCommandWrapper;
import org.bukkit.craftbukkit.v1_19_R2.util.permissions.CraftDefaultPermissions;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.permissions.DefaultPermissions;
import org.spigotmc.SpigotConfig;

import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CommandAPI {

	private final Logger logger;
	private final String prefix;
	private final CommandDispatcher dispatcher = new CommandDispatcher();
	private final List<Commands> commands = new ArrayList<Commands>();

	public CommandAPI(Plugin plugin) {
		this(plugin.getName(), plugin.getLogger());
	}

	@Deprecated
	public CommandAPI(String prefix) {
		this(prefix, Bukkit.getLogger());
	}

	public CommandAPI(String prefix, Logger logger) {
		this.prefix = prefix;
		this.logger = logger;
	}

	public CommandDispatcher getDispatcher() {
		return dispatcher;
	}

	public List<Commands> getCommands() {
		return commands;
	}

	public String getPrefix() {
		return prefix;
	}

	public void register(BrigadierCommand command) {
		String[] names = command.names;
		String[] permissions = command.permissions;
		CommandUser[] user = command.user;

		for(Annotation annotation : command.getClass().getAnnotations()) {
			if(!(annotation instanceof CommandInfo commandInfo))
				continue;
			names = commandInfo.names();
			permissions = commandInfo.permissions();
			user = commandInfo.user();
			break;
		}

		for (String name : names) {
			Commands c = Commands.literal(name);

			c.perm(permissions);
			c.user(user);
			c.updateRequirements();

			command.command(c);
			commands.add(c);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void finish() {
		//register to dispatcher
		for(Commands c : commands)
			dispatcher.a().register((LiteralArgumentBuilder<CommandListenerWrapper>) c.b);
		logger.info("§bRegistered " + commands.size() + " commands!");
		
		//register to server
		setCommands(dispatcher, true);
		((CraftServer) Bukkit.getServer()).getCommandMap().setFallbackCommands();
	    setCommands(dispatcher, false);
	    ((CraftServer) Bukkit.getServer()).getCommandMap().registerServerAliases();
	    DefaultPermissions.registerCorePermissions();
	    CraftDefaultPermissions.registerCorePermissions();

	}
	
	private void setCommands(CommandDispatcher dispatcher, boolean first) {
		for (CommandNode<CommandListenerWrapper> cmd : dispatcher.a().getRoot().getChildren()) {
			VanillaCommandWrapper wrapper = new VanillaCommandWrapper(dispatcher, cmd);
			wrapper.setPermission(null);
			if (SpigotConfig.replaceCommands.contains(wrapper.getName())) {
				if (first)
					((CraftServer) Bukkit.getServer()).getCommandMap().register(prefix, wrapper);
				continue;
			}
			if (!first)
				((CraftServer) Bukkit.getServer()).getCommandMap().register(prefix, wrapper);
		}
	}
	
	/*public static Location vec3ToLocation(Vec3D vec, World world) {
		return new Location(world, vec.b, vec.c, vec.d);
	}
	
	public static Location blockPositionToLocation(BlockPosition pos, World world) {
		return new Location(world, pos.getX(), pos.getY(), pos.getZ());
	}
	
	public static Location blockPositionToLocation(BlockPosition2D pos, World world) {
		return new Location(world, pos.a, 0, pos.b);
	}
	
	public static String blockPositionToString(BlockPosition pos) {
		return "§f[§6" + pos.b() + "§f, §6" + pos.c() + "§f, §6" + pos.d() + "§f]";
	}
	
	public static String blockPositionToString(BlockPosition2D pos) {
		return "§f[§6" + pos.a + "§f, §6" + pos.b + "§f]";
	}

	public static Material getMaterialByArgumentTileLocation(ArgumentTileLocation tile) {
		return Material.getMaterial(tile.a().getBlock().getItem().toString().toUpperCase());
	}*/

}
