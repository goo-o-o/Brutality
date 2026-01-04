package net.goo.brutality.network;

import net.goo.brutality.gui.CooldownMeter;
import net.goo.brutality.util.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundUpdateAbilityCooldownsPacket {

    final int tickCount;
    final ItemStack itemStack;
    final Constants.CooldownType cooldownType;

    public ClientboundUpdateAbilityCooldownsPacket(Constants.CooldownType cooldownType, int tickCount, ItemStack itemStack) {
        this.cooldownType = cooldownType;
        this.tickCount = tickCount;
        this.itemStack = itemStack;
    }

    public ClientboundUpdateAbilityCooldownsPacket(FriendlyByteBuf buf) {
        this.cooldownType = buf.readEnum(Constants.CooldownType.class);
        this.tickCount = buf.readInt();
        this.itemStack = buf.readItem();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(cooldownType);
        buf.writeInt(tickCount);
        buf.writeItem(itemStack);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            switch (cooldownType) {
                case ABILITY -> {
                    CooldownMeter.AbilityCooldownMeter.maxTicks = tickCount;
                    CooldownMeter.AbilityCooldownMeter.itemStack = itemStack;
                }
                case ARMOR_SET -> {
                    CooldownMeter.ArmorSetAbilityCooldownMeter.maxTicks = tickCount;
                    CooldownMeter.ArmorSetAbilityCooldownMeter.itemStack = itemStack;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}