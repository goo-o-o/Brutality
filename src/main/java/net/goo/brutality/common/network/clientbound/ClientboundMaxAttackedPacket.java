package net.goo.brutality.common.network.clientbound;

import net.goo.brutality.client.ClientProxy;
import net.goo.brutality.common.network.IBrutalityPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundMaxAttackedPacket implements IBrutalityPacket<ClientboundMaxAttackedPacket> {
    private final int entityId;

    // Server-side constructor
    public ClientboundMaxAttackedPacket(int entityId) {
        this.entityId = entityId;
    }

    // Decoder (Reading from buffer)
    public ClientboundMaxAttackedPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
    }

    // Encoder (Writing to buffer)
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
    }

    public void handle(ClientboundMaxAttackedPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ClientProxy.startMaxAttack(packet.entityId));
        ctx.get().setPacketHandled(true);
    }
}