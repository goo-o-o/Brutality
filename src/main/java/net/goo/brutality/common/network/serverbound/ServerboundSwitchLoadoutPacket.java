package net.goo.brutality.common.network.serverbound;

import net.goo.brutality.common.entity.capabilities.BrutalityCapabilities;
import net.goo.brutality.common.network.IBrutalityPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundSwitchLoadoutPacket implements IBrutalityPacket {
    int index;

    public ServerboundSwitchLoadoutPacket(int index) {
        this.index = index;
    }

    public ServerboundSwitchLoadoutPacket(FriendlyByteBuf buf) {
        this.index = buf.readInt();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.index);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                player.getCapability(BrutalityCapabilities.LOADOUTS).ifPresent(cap -> {
                    cap.switchLoadout(player, this.index);
                    BrutalityCapabilities.sync(player, BrutalityCapabilities.LOADOUTS);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}