package io.github.mortuusars.exposure_polaroid.network.fabric;

import io.github.mortuusars.exposure.fabric.ExposureFabric;
import io.github.mortuusars.exposure_polaroid.ExposurePolaroid;
import io.github.mortuusars.exposure_polaroid.network.packet.Packet;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class PacketsImpl {
    public static void sendToServer(Packet packet) {
        FabricC2SPackets.sendToServer(packet);
    }

    public static void sendToClient(Packet packet, ServerPlayer player) {
        ServerPlayNetworking.send(player, packet);
    }

    public static void sendToClients(Packet packet, Predicate<ServerPlayer> filter) {
        if (ExposureFabric.server == null) {
            ExposurePolaroid.LOGGER.error("Cannot send a packet to players. Server is not available.");
            return;
        }

        for (ServerPlayer player : ExposureFabric.server.getPlayerList().getPlayers()) {
            if (filter.test(player)) {
                sendToClient(packet, player);
            }
        }
    }

    public static void sendToAllClients(Packet packet) {
        if (ExposureFabric.server == null) {
            ExposurePolaroid.LOGGER.error("Cannot send a packet to all players. Server is not available.");
            return;
        }

        for (ServerPlayer player : ExposureFabric.server.getPlayerList().getPlayers()) {
            sendToClient(packet, player);
        }
    }

    public static void sendToPlayersNear(Packet packet, @NotNull ServerLevel level, @Nullable ServerPlayer excludedPlayer,
                                         double x, double y, double z, double radius) {
        sendToClients(packet, player -> {
            if (player != excludedPlayer && player.level().dimension() == level.dimension()) {
                double d0 = x - player.getX();
                double d1 = y - player.getY();
                double d2 = z - player.getZ();
                return d0 * d0 + d1 * d1 + d2 * d2 < radius * radius;
            }

            return false;
        });
    }
}