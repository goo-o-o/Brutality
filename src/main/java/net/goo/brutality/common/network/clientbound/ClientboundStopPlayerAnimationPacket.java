package net.goo.brutality.common.network.clientbound;

import net.goo.brutality.client.player_animation.AnimationHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ClientboundStopPlayerAnimationPacket {
    UUID playerId;
    int fadeOutTicks;

    public ClientboundStopPlayerAnimationPacket(UUID playerId, int fadeOutTicks) {
        this.playerId = playerId;
        this.fadeOutTicks = fadeOutTicks;
    }

    public ClientboundStopPlayerAnimationPacket(FriendlyByteBuf buf) {
        this.playerId = buf.readUUID();
        this.fadeOutTicks = buf.readInt();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(this.playerId);
        buf.writeInt(this.fadeOutTicks);
    }


    public static void handle(ClientboundStopPlayerAnimationPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> AnimationHelper.stopAnimation(packet.playerId, packet.fadeOutTicks));
        ctx.get().setPacketHandled(true);
    }
}