package net.goo.brutality.common.network.serverbound;

import net.goo.brutality.common.item.armor.BrutalityArmorMaterials;
import net.goo.brutality.common.item.armor.VampireLordArmorItem;
import net.goo.brutality.common.network.IBrutalityPacket;
import net.goo.brutality.util.ModUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundArmorSetBonusAbilityPressPacket implements IBrutalityPacket {
    public ServerboundArmorSetBonusAbilityPressPacket() {
    }

    public ServerboundArmorSetBonusAbilityPressPacket(FriendlyByteBuf buf) {
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
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