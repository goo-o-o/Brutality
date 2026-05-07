package net.goo.brutality.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface IBrutalityPacket {
    // cannot be named write() as it inteferes with minecrafts packet definition
    void encode(FriendlyByteBuf buf); // make sure to do @Override for non-dev env

    void handle(Supplier<NetworkEvent.Context> ctx);
}