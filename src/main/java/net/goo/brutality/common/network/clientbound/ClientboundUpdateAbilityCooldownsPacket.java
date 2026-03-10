package net.goo.brutality.common.network.clientbound;

import net.goo.brutality.client.gui.misc_elements.CooldownMeter;
import net.goo.brutality.common.network.IBrutalityPacket;
import net.goo.brutality.util.CommonConstants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundUpdateAbilityCooldownsPacket implements IBrutalityPacket<ClientboundUpdateAbilityCooldownsPacket> {

    final int tickCount;
    final ItemStack itemStack;
    final CommonConstants.CooldownType cooldownType;

    public ClientboundUpdateAbilityCooldownsPacket(CommonConstants.CooldownType cooldownType, int tickCount, ItemStack itemStack) {
        this.cooldownType = cooldownType;
        this.tickCount = tickCount;
        this.itemStack = itemStack;
    }

    public ClientboundUpdateAbilityCooldownsPacket(FriendlyByteBuf buf) {
        this.cooldownType = buf.readEnum(CommonConstants.CooldownType.class);
        this.tickCount = buf.readInt();
        this.itemStack = buf.readItem();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(cooldownType);
        buf.writeInt(tickCount);
        buf.writeItem(itemStack);
    }

    @Override
    public void handle(ClientboundUpdateAbilityCooldownsPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            switch (cooldownType) {
                case ABILITY -> {
                    CooldownMeter.AbilityCooldownMeter.maxTicks = packet.tickCount;
                    CooldownMeter.AbilityCooldownMeter.itemStack = packet.itemStack;
                }
                case ARMOR_SET -> {
                    CooldownMeter.ArmorSetAbilityCooldownMeter.maxTicks = packet.tickCount;
                    CooldownMeter.ArmorSetAbilityCooldownMeter.itemStack = packet.itemStack;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}