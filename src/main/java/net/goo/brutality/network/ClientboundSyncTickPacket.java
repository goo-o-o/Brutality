package net.goo.brutality.network;

import net.goo.brutality.event.forge.client.ClientTickHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundSyncTickPacket {
    private final long tick;

    public ClientboundSyncTickPacket(long tick) {
        this.tick = tick;
    }

    public ClientboundSyncTickPacket(FriendlyByteBuf buf) {
        this.tick = buf.readLong();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeLong(tick);
    }


    public static void handle(ClientboundSyncTickPacket packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide().isClient()) {
                ClientTickHandler.setClientTick(packet.tick);
            }
        });
        context.setPacketHandled(true);
    }
}