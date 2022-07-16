package pl.tuso.entities.util;

import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.world.entity.Entity;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public final class Packets {
    public @NotNull ClientboundTeleportEntityPacket teleportEntityPacket(Entity entity, Location location) {
        ClientboundTeleportEntityPacket teleportEntityPacket = new ClientboundTeleportEntityPacket(entity);
        // TODO
        return teleportEntityPacket;
    }
}
