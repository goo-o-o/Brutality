package net.goo.armament.util;

import net.bettercombat.api.client.BetterCombatClientEvents;
import net.goo.armament.item.weapon.custom.ExcaliburSword;
import net.goo.armament.item.noir.ShadowstepSword;
import net.goo.armament.item.terra.TerraBladeSword;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

public class BetterCombatIntegration {
    public static void register() {
        if (ModList.get().isLoaded("bettercombat")) {
            BetterCombatClientEvents.ATTACK_START.register((player, hand) -> {
                ItemStack stack = player.getMainHandItem();
                Item item = stack.getItem();
                System.out.println("LOADED");
                if (item instanceof TerraBladeSword terraBladeSword) {
                    terraBladeSword.performTerraBeam(stack, player);
                } else if (item instanceof ExcaliburSword excaliburSword) {
                    excaliburSword.performExcaliburBeam(stack, player);
                } else if (item instanceof ShadowstepSword) {
                    return;
                }

            });
        }
    }
}


