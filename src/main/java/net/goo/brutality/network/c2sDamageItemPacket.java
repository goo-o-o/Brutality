package net.goo.brutality.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class c2sDamageItemPacket {
    private final int damage;
    private final boolean isMainHand;

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
        ItemStack stack = packet.isMainHand ? sender.getMainHandItem() : sender.getOffhandItem();
        EquipmentSlot slot = packet.isMainHand ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                stack.hurtAndBreak(packet.damage, sender, (consumer) -> {
                    consumer.broadcastBreakEvent(slot);
                });

            }
        });

        ctx.get().setPacketHandled(true);
    }
}
