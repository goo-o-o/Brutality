package net.goo.brutality.util;

import net.bettercombat.api.client.BetterCombatClientEvents;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.ServerboundBetterCombatAttackStartListenerPacket;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class BetterCombatIntegration {
    public static void register() {
        BetterCombatClientEvents.ATTACK_START.register((player, hand) -> {
            if (!(player.level() instanceof ClientLevel)) return;
            if (hand == null) return;
            ItemStack stack = hand.isOffHand() ? player.getOffhandItem() : player.getMainHandItem();
            PacketHandler.sendToServer(new ServerboundBetterCombatAttackStartListenerPacket(stack, hand.combo().current()));
        });

    }
}


