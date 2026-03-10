package net.goo.brutality.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface IBrutalityPacket<T extends IBrutalityPacket<T>> {
    void write(FriendlyByteBuf buf);

    void handle(T packet, Supplier<NetworkEvent.Context> ctx);
}