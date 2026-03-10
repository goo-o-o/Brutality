package net.goo.brutality.common.network.serverbound;

import net.goo.brutality.common.network.IBrutalityPacket;
import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.common.network.clientbound.ClientboundStopPlayerAnimationPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * Triggers an animation from client-side to be synchronized across other clients.
 */
public class ServerboundStopPlayerAnimationPacket implements IBrutalityPacket<ServerboundStopPlayerAnimationPacket> {
    UUID playerId;
    int fadeOutTicks;

    public ServerboundStopPlayerAnimationPacket(UUID playerId, int fadeOutTicks) {
        this.playerId = playerId;
        this.fadeOutTicks = fadeOutTicks;
    }

    public ServerboundStopPlayerAnimationPacket(FriendlyByteBuf buf) {
        this.playerId = buf.readUUID();
        this.fadeOutTicks = buf.readInt();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(this.playerId);
        buf.writeInt(this.fadeOutTicks);
    }

    public void handle(ServerboundStopPlayerAnimationPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;
            ServerLevel level = sender.serverLevel();
            for (ServerPlayer player : level.players()) {
                if (player != ctx.get().getSender()) {
                    PacketHandler.sendToPlayerClient(new ClientboundStopPlayerAnimationPacket(packet.playerId, packet.fadeOutTicks), player);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}