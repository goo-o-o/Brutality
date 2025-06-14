package net.goo.brutality.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class c2sChangeNBTPacket {
    private final CompoundTag modifications;
    private final boolean isMainHand;

    public c2sChangeNBTPacket(CompoundTag modifications, boolean isMainHand) {
        this.modifications = modifications;
        this.isMainHand = isMainHand;
    }

    public c2sChangeNBTPacket(FriendlyByteBuf buf) {
        this.modifications = buf.readNbt();
        this.isMainHand = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(modifications);
        buf.writeBoolean(isMainHand);
    }

    public static void handle(c2sChangeNBTPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            ItemStack stack = packet.isMainHand ? sender.getMainHandItem() : sender.getOffhandItem();
            CompoundTag tag = stack.getOrCreateTag();

            for (String key : packet.modifications.getAllKeys()) {
                tag.put(key, Objects.requireNonNull(packet.modifications.get(key)));
            }

        });
        ctx.get().setPacketHandled(true);
    }
}