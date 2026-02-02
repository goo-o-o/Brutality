package net.goo.brutality.common.network.serverbound;

import net.goo.brutality.util.build_archetypes.RageHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundActivateRagePacket {
    public ServerboundActivateRagePacket() {
    }

    public ServerboundActivateRagePacket(FriendlyByteBuf buf) {
    }

    public void write(FriendlyByteBuf buf) {
    }

    public static void handle(ServerboundActivateRagePacket packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender == null) return;
            RageHelper.tryTriggerRage(sender);
        });
        context.setPacketHandled(true);
    }
}