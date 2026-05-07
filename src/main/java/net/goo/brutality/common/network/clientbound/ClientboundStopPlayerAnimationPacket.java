package net.goo.brutality.common.network.clientbound;

import net.goo.brutality.client.player_animation.AnimationHelper;
import net.goo.brutality.common.network.IBrutalityPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ClientboundStopPlayerAnimationPacket implements IBrutalityPacket {
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
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(this.playerId);
        buf.writeInt(this.fadeOutTicks);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> AnimationHelper.stopAnimation(this.playerId, this.fadeOutTicks));
        ctx.get().setPacketHandled(true);
    }
}