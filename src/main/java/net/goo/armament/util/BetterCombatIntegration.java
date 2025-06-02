package net.goo.armament.util;

import net.bettercombat.api.client.BetterCombatClientEvents;
import net.goo.armament.item.terra.TerraBladeSword;
import net.goo.armament.item.weapon.custom.ExcaliburSword;
import net.goo.armament.item.weapon.custom.SupernovaSword;
import net.goo.armament.network.PacketHandler;
import net.goo.armament.network.c2sTriggerAnimPacket;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import software.bernie.geckolib.animatable.GeoItem;

public class BetterCombatIntegration {


    public static void register() {
        if (ModList.get().isLoaded("bettercombat")) {
            BetterCombatClientEvents.ATTACK_START.register((player, hand) -> {
                ItemStack stack = player.getMainHandItem();
                Item item = stack.getItem();
                if (item instanceof TerraBladeSword terraBladeSword) {
                    terraBladeSword.performTerraBeam(stack, player);
                } else if (item instanceof ExcaliburSword excaliburSword) {
                    excaliburSword.performExcaliburBeam(stack, player);
                } else if (item instanceof SupernovaSword) {
                    if (hand.combo().current() == 2) {

                        PacketHandler.sendToServer(new c2sTriggerAnimPacket(stack, GeoItem.getId(stack), "controller", "stab"));
//                        ((SupernovaSword) item).triggerAnim(player, GeoItem.getId(stack), "controller", "stab");
//                        System.out.println(GeoItem.getId(stack));
                    } else {
                        PacketHandler.sendToServer(new c2sTriggerAnimPacket(stack, GeoItem.getId(stack), "controller", "swing"));
//                        ((SupernovaSword) item).triggerAnim(player, GeoItem.getId(stack), "controller", "swing");
//                        System.out.println(GeoItem.getId(stack));
                    }
                }

            });
        }
    }
}


