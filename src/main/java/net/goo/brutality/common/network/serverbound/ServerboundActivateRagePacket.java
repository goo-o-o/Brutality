package net.goo.brutality.common.network.serverbound;

import net.goo.brutality.common.network.IBrutalityPacket;
import net.goo.brutality.util.build_archetypes.RageHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundActivateRagePacket implements IBrutalityPacket {
    public ServerboundActivateRagePacket() {
    }

    public ServerboundActivateRagePacket(FriendlyByteBuf buf) {
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender == null) return;
            RageHelper.tryTriggerRage(sender);
        });
        context.setPacketHandled(true);
    }
}