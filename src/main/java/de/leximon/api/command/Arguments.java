package de.leximon.api.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.arguments.ArgumentDimension;
import net.minecraft.commands.arguments.ArgumentEntity;
import net.minecraft.commands.arguments.coordinates.ArgumentPosition;
import net.minecraft.commands.arguments.coordinates.ArgumentRotation;
import net.minecraft.commands.arguments.coordinates.ArgumentVec3;
import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.phys.Vec2F;
import net.minecraft.world.phys.Vec3D;

import java.util.Collection;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class Arguments {

    public static class Entity {
        public static ArgumentType<?> entity() {
            return ArgumentEntity.a();
        }
        public static ArgumentType<?> entities() {
            return ArgumentEntity.b();
        }
        public static ArgumentType<?> player() {
            return ArgumentEntity.c();
        }
        public static ArgumentType<?> players() {
            return ArgumentEntity.d();
        }

        public static org.bukkit.entity.Entity getEntity(CommandContext<?> context, String name) throws CommandSyntaxException {
            return ArgumentEntity.a((CommandContext<CommandListenerWrapper>) context, name).getBukkitEntity();
        }
        public static Collection<org.bukkit.entity.Entity> getEntities(CommandContext<?> context, String name) throws CommandSyntaxException {
            return ArgumentEntity.b((CommandContext<CommandListenerWrapper>) context, name).stream()
                    .map(net.minecraft.world.entity.Entity::getBukkitEntity)
                    .collect(Collectors.toList());
        }
        public static Collection<org.bukkit.entity.Entity> getOptionalEntities(CommandContext<?> context, String name) throws CommandSyntaxException {
            return ArgumentEntity.c((CommandContext<CommandListenerWrapper>) context, name).stream()
                    .map(net.minecraft.world.entity.Entity::getBukkitEntity)
                    .collect(Collectors.toList());
        }
        public static Collection<org.bukkit.entity.Player> getPlayers(CommandContext<?> context, String name) throws CommandSyntaxException {
            return ArgumentEntity.f((CommandContext<CommandListenerWrapper>) context, name).stream()
                    .map(EntityPlayer::getBukkitEntity)
                    .collect(Collectors.toList());
        }
        public static Collection<org.bukkit.entity.Player> getOptionalPlayers(CommandContext<?> context, String name) throws CommandSyntaxException {
            return ArgumentEntity.d((CommandContext<CommandListenerWrapper>) context, name).stream()
                    .map(EntityPlayer::getBukkitEntity)
                    .collect(Collectors.toList());
        }
    }

    public static class Location {
        public static ArgumentType<?> location() {
            return ArgumentVec3.a();
        }

        public static org.bukkit.Location getLocation(CommandContext<?> context, String name) {
            Vec3D vec = ArgumentVec3.a((CommandContext<CommandListenerWrapper>) context, name);
            return new org.bukkit.Location(((CommandContext<CommandListenerWrapper>) context).getSource().e().getWorld(), vec.b, vec.c, vec.d);
        }
    }

    public static class BlockLocation {
        public static ArgumentType<?> blockLocation() {
            return ArgumentPosition.a();
        }

        public static org.bukkit.Location getLoadedBlockLocation(CommandContext<?> context, String name) throws CommandSyntaxException {
            BlockPosition pos = ArgumentPosition.a((CommandContext<CommandListenerWrapper>) context, name);
            return new org.bukkit.Location(((CommandContext<CommandListenerWrapper>) context).getSource().e().getWorld(), pos.u(), pos.v(), pos.w());
        }

        public static org.bukkit.Location getBlockLocation(CommandContext<?> context, String name) throws CommandSyntaxException {
            BlockPosition pos = ArgumentPosition.b((CommandContext<CommandListenerWrapper>) context, name);
            return new org.bukkit.Location(((CommandContext<CommandListenerWrapper>) context).getSource().e().getWorld(), pos.u(), pos.v(), pos.w());
        }
    }

    public static class World {
        public static ArgumentType<?> world() {
            return ArgumentDimension.a();
        }

        public static org.bukkit.World getWorld(CommandContext<?> context, String name) throws CommandSyntaxException {
            return ArgumentDimension.a((CommandContext<CommandListenerWrapper>) context, name).getWorld();
        }
    }

    public static class Rotation {
        public static ArgumentType<?> rotation() {
            return ArgumentRotation.a();
        }

        public static float[] getRotation(CommandContext<?> context, String name) {
            Vec2F rot = ArgumentRotation.a((CommandContext<CommandListenerWrapper>) context, name).b(((CommandContext<CommandListenerWrapper>) context).getSource());
            return new float[] {rot.i, rot.j};
        }
    }

}
