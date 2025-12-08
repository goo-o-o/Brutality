package net.goo.brutality.network;

import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.goo.brutality.item.base.BrutalityThrowingItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundHandleThrowingProjectilePacket {
    private final ItemStack stack;

    public ServerboundHandleThrowingProjectilePacket(ItemStack stack) {
        this.stack = stack;
    }

    public ServerboundHandleThrowingProjectilePacket(FriendlyByteBuf buf) {
        this.stack = buf.readItem();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeItem(this.stack);
    }

    public static void handle(ServerboundHandleThrowingProjectilePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null || !(packet.stack.getItem() instanceof BrutalityThrowingItem throwingItem)) return;
            DelayedTaskScheduler.queueServerWork(player.serverLevel(), 6, () -> throwingItem.handleThrowPacket(packet.stack, player));
            ctx.get().setPacketHandled(true);
        });
    }
}