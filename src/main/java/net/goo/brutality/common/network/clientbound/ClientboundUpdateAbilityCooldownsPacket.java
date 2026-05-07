package net.goo.brutality.common.network.clientbound;

import net.goo.brutality.client.gui.misc_elements.CooldownMeter;
import net.goo.brutality.common.network.IBrutalityPacket;
import net.goo.brutality.util.CommonConstants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundUpdateAbilityCooldownsPacket implements IBrutalityPacket {

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

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(cooldownType);
        buf.writeInt(tickCount);
        buf.writeItem(itemStack);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            switch (cooldownType) {
                case ABILITY -> {
                    CooldownMeter.AbilityCooldownMeter.maxTicks = this.tickCount;
                    CooldownMeter.AbilityCooldownMeter.itemStack = this.itemStack;
                }
                case ARMOR_SET -> {
                    CooldownMeter.ArmorSetAbilityCooldownMeter.maxTicks = this.tickCount;
                    CooldownMeter.ArmorSetAbilityCooldownMeter.itemStack = this.itemStack;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}