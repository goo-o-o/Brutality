package net.goo.brutality.common.network.clientbound;

import net.goo.brutality.client.ClientProxy;
import net.goo.brutality.common.network.IBrutalityPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundMaxAttackedPacket implements IBrutalityPacket {
    private final int entityId;

    // Server-side constructor
    public ClientboundMaxAttackedPacket(int entityId) {
        this.entityId = entityId;
    }

    // Decoder (Reading from buffer)
    public ClientboundMaxAttackedPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ClientProxy.startMaxAttack(this.entityId));
        ctx.get().setPacketHandled(true);
    }
}