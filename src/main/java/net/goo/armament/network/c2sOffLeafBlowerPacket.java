package net.goo.armament.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class c2sOffLeafBlowerPacket {
    private static final String ACTIVE_KEY = "LeafBlowerActive";

    public c2sOffLeafBlowerPacket() {
    }

    public c2sOffLeafBlowerPacket(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buffer) {
    }

    public static void handle(c2sOffLeafBlowerPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ItemStack mainHandItem = ctx.get().getSender().getMainHandItem();
        ItemStack offHandItem = ctx.get().getSender().getOffhandItem();
        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                mainHandItem.getOrCreateTag().putBoolean(ACTIVE_KEY, false);
                offHandItem.getOrCreateTag().putBoolean(ACTIVE_KEY, false);
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
