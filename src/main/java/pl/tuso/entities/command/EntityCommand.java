package pl.tuso.entities.command;

import com.google.common.collect.ImmutableList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import org.apache.commons.lang3.Validate;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.tuso.entities.type.Types;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EntityCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length != 1) {
                sender.sendMessage(Component.text("Usage: ", TextColor.color(220, 38, 37)).append(Component.text("/" + command.getLabel() + " <entity>", TextColor.color(255, 255, 255))));
                return false;
            }
            Optional<EntityType> type = Types.VALUES.stream().filter(entityType -> entityType.getDescriptionId().equals(args[0])).findAny();
            if (type.isEmpty()) {
                sender.sendMessage(Component.text("This entity doesn't exist, or does it?", TextColor.color(220, 38, 37)));
                return false;
            }
            Location location = player.getLocation();
            Entity entity = type.get().create(((CraftWorld) player.getWorld()).getHandle());
            entity.setPos(location.getX(), location.getY(), location.getZ());
            ((CraftWorld) player.getWorld()).getHandle().addFreshEntity(entity);
            sender.sendMessage(Component.text("Created " + args[0], TextColor.color(133, 204, 21)));
            return true;
        } else {
            sender.sendMessage(Component.text("Only player can use this command!", TextColor.color(220, 38, 37)));
            return false;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return Types.VALUES.stream().map(entityType -> entityType.getDescriptionId()).collect(Collectors.toList());
        } else {
            return ImmutableList.of();
        }
    }

    public void register(PluginCommand command) {
        Validate.notNull(command, "command");
        command.setExecutor(this::onCommand);
        command.setTabCompleter(this::onTabComplete);
    }
}
