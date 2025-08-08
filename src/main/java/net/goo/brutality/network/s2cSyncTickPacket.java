package net.goo.brutality.network;

import net.goo.brutality.event.forge.client.ClientTickHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class s2cSyncTickPacket {
    private final long tick;

    public s2cSyncTickPacket(long tick) {
        this.tick = tick;
    }

    public s2cSyncTickPacket(FriendlyByteBuf buf) {
        this.tick = buf.readLong();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeLong(tick);
    }


    public static void handle(s2cSyncTickPacket packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide().isClient()) {
                ClientTickHandler.setClientTick(packet.tick);
            }
        });
        context.setPacketHandled(true);
    }
}