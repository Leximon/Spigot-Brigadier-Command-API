package de.leximon.api.command;

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

public class Arguments {

    public static class Entity {
        public static ArgumentEntity entity() {
            return ArgumentEntity.a();
        }
        public static ArgumentEntity entities() {
            return ArgumentEntity.b();
        }
        public static ArgumentEntity player() {
            return ArgumentEntity.c();
        }
        public static ArgumentEntity players() {
            return ArgumentEntity.d();
        }

        public static org.bukkit.entity.Entity getEntity(CommandContext<CommandListenerWrapper> context, String name) throws CommandSyntaxException {
            return ArgumentEntity.a(context, name).getBukkitEntity();
        }
        public static Collection<org.bukkit.entity.Entity> getEntities(CommandContext<CommandListenerWrapper> context, String name) throws CommandSyntaxException {
            return ArgumentEntity.b(context, name).stream()
                    .map(net.minecraft.world.entity.Entity::getBukkitEntity)
                    .collect(Collectors.toList());
        }
        public static Collection<org.bukkit.entity.Entity> getOptionalEntities(CommandContext<CommandListenerWrapper> context, String name) throws CommandSyntaxException {
            return ArgumentEntity.c(context, name).stream()
                    .map(net.minecraft.world.entity.Entity::getBukkitEntity)
                    .collect(Collectors.toList());
        }
        public static Collection<org.bukkit.entity.Player> getPlayers(CommandContext<CommandListenerWrapper> context, String name) throws CommandSyntaxException {
            return ArgumentEntity.f(context, name).stream()
                    .map(EntityPlayer::getBukkitEntity)
                    .collect(Collectors.toList());
        }
        public static Collection<org.bukkit.entity.Player> getOptionalPlayers(CommandContext<CommandListenerWrapper> context, String name) throws CommandSyntaxException {
            return ArgumentEntity.d(context, name).stream()
                    .map(EntityPlayer::getBukkitEntity)
                    .collect(Collectors.toList());
        }
    }

    public static class Location {
        public static ArgumentVec3 location() {
            return ArgumentVec3.a();
        }

        public static org.bukkit.Location getLocation(CommandContext<CommandListenerWrapper> context, String name) {
            Vec3D vec = ArgumentVec3.a(context, name);
            return new org.bukkit.Location(context.getSource().e().getWorld(), vec.b, vec.c, vec.d);
        }
    }

    public static class BlockLocation {
        public static ArgumentPosition blockLocation() {
            return ArgumentPosition.a();
        }

        public static org.bukkit.Location getLoadedBlockLocation(CommandContext<CommandListenerWrapper> context, String name) throws CommandSyntaxException {
            BlockPosition pos = ArgumentPosition.a(context, name);
            return new org.bukkit.Location(context.getSource().e().getWorld(), pos.u(), pos.v(), pos.w());
        }

        public static org.bukkit.Location getBlockLocation(CommandContext<CommandListenerWrapper> context, String name) throws CommandSyntaxException {
            BlockPosition pos = ArgumentPosition.b(context, name);
            return new org.bukkit.Location(context.getSource().e().getWorld(), pos.u(), pos.v(), pos.w());
        }
    }

    public static class World {
        public static ArgumentDimension world() {
            return ArgumentDimension.a();
        }

        public static org.bukkit.World getWorld(CommandContext<CommandListenerWrapper> context, String name) throws CommandSyntaxException {
            return ArgumentDimension.a(context, name).getWorld();
        }
    }

    public static class Rotation {
        public static ArgumentRotation rotation() {
            return ArgumentRotation.a();
        }

        public static float[] getRotation(CommandContext<CommandListenerWrapper> context, String name) {
            Vec2F rot = ArgumentRotation.a(context, name).b(context.getSource());
            return new float[] {rot.i, rot.j};
        }
    }

}
