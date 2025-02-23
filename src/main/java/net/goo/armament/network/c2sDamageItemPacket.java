package net.goo.armament.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class c2sDamageItemPacket {
    private int damage;
    private boolean isMainHand;

    public c2sDamageItemPacket(int damage, boolean isMainHand) {
        this.damage = damage;
        this.isMainHand = isMainHand;
    }

    public c2sDamageItemPacket(FriendlyByteBuf buf) {
        this.damage = buf.readInt();
        this.isMainHand = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(damage);
        buf.writeBoolean(isMainHand);
    }

    public static void handle(c2sDamageItemPacket packet, Supplier<NetworkEvent.Context> ctx) {
        Player sender = ctx.get().getSender();
        if (sender == null) return;
        ItemStack mainHandItem = sender.getMainHandItem();
        ItemStack offHandItem = sender.getOffhandItem();
        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                if (packet.isMainHand) {
                    mainHandItem.hurtAndBreak(packet.damage, sender, (consumer) -> {
                        consumer.broadcastBreakEvent(sender.getUsedItemHand());
                    });
                } else {
                    offHandItem.hurtAndBreak(packet.damage, sender, (consumer) -> {
                        consumer.broadcastBreakEvent(sender.getUsedItemHand());
                    });
                }
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
