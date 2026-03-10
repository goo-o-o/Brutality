package net.goo.brutality.common.network.serverbound;

import net.goo.brutality.common.entity.capabilities.BrutalityCapabilities;
import net.goo.brutality.common.network.IBrutalityPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundAddLoadoutPacket implements IBrutalityPacket<ServerboundAddLoadoutPacket> {
    String name;
    public ServerboundAddLoadoutPacket(String name) {
        this.name = name;
    }

    public ServerboundAddLoadoutPacket(FriendlyByteBuf buf) {
        this.name = buf.readUtf();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.name);
    }

    public void handle(ServerboundAddLoadoutPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                player.getCapability(BrutalityCapabilities.LOADOUTS).ifPresent(cap -> {
                    cap.addLoadout(player, msg.name);
                    BrutalityCapabilities.sync(player, BrutalityCapabilities.LOADOUTS);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}