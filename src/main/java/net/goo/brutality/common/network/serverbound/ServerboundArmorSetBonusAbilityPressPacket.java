package net.goo.brutality.common.network.serverbound;

import net.goo.brutality.common.item.BrutalityArmorMaterials;
import net.goo.brutality.common.item.armor.VampireLordArmorItem;
import net.goo.brutality.util.ModUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundArmorSetBonusAbilityPressPacket {
    public ServerboundArmorSetBonusAbilityPressPacket() {
    }

    public ServerboundArmorSetBonusAbilityPressPacket(FriendlyByteBuf buf) {
    }

    public void write(FriendlyByteBuf buf) {
    }

    public static void handle(ServerboundArmorSetBonusAbilityPressPacket packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender == null) return;

            if (ModUtils.hasFullArmorSet(sender, BrutalityArmorMaterials.VAMPIRE_LORD)) {
                VampireLordArmorItem.handleArmorSetAbility(sender);
            }

        });
        context.setPacketHandled(true);
    }
}