package net.goo.brutality.event.forge;

import net.goo.brutality.common.item.generic.augments.BrutalityAugmentItem;
import net.goo.brutality.common.item.generic.augments.BrutalityAugmentationDevice;
import net.goo.brutality.common.item.generic.augments.BrutalitySealAugmentItem;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.AugmentHelper;
import net.goo.brutality.util.item.ItemCategoryUtils;
import net.goo.brutality.util.item.StatTrakUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeAnvilHandler {

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        // 1. Handle Seals
        if (right.getItem() instanceof BrutalityAugmentationDevice augmentationItem &&
                !(left.getItem() instanceof ICurioItem || left.getItem() instanceof BrutalityAugmentationDevice || left.getItem() instanceof BrutalityAugmentItem)) {
            ItemStack output = left.copy();
            if (AugmentHelper.addAugmentSlot(output, augmentationItem)) {
                event.setOutput(output);
                event.setCost(5);
                event.setMaterialCost(1);

                if (event.getName() != null && !event.getName().isEmpty()) {
                    output.setHoverName(Component.literal(event.getName()));
                }
                return;
            }
        }

        if (right.getItem() instanceof BrutalitySealAugmentItem &&
                !(left.getItem() instanceof ICurioItem || left.getItem() instanceof BrutalitySealAugmentItem)) {
            ItemStack output = left.copy();
            if (!AugmentHelper.addAugments(output, right).isEmpty()) {
                event.setOutput(output);
                event.setCost(5);
                event.setMaterialCost(1);

                if (event.getName() != null && !event.getName().isEmpty()) {
                    output.setHoverName(Component.literal(event.getName()));
                }
                return; // Exit so we don't hit the weapon check below
            }
        }

        // 2. Handle StatTraks
        if (ItemCategoryUtils.isTool(left) || ItemCategoryUtils.isWeapon(left)) {
            boolean isStatTrak = false;
            ItemStack output = left.copy();

            if (right.is(BrutalityItems.BASIC_STAT_TRAKKER.get())) {
                StatTrakUtils.addStatTrak(output, StatTrakUtils.StatTrakVariant.BASIC);
                isStatTrak = true;
            } else if (right.is(BrutalityItems.GOLDEN_STAT_TRAKKER.get())) {
                StatTrakUtils.addStatTrak(output, StatTrakUtils.StatTrakVariant.GOLD);
                isStatTrak = true;
            } else if (right.is(BrutalityItems.PRISMATIC_STAT_TRAKKER.get())) {
                StatTrakUtils.addStatTrak(output, StatTrakUtils.StatTrakVariant.PRISMATIC);
                isStatTrak = true;
            }

            if (isStatTrak) {
                // Apply the name here too!
                if (event.getName() != null && !event.getName().isEmpty()) {
                    output.setHoverName(Component.literal(event.getName()));
                }
                event.setOutput(output);
                event.setCost(5);
                event.setMaterialCost(1);
            }
        }

        // 3. IMPORTANT: If none of the above happened, DO NOT call event.setOutput().
        // By leaving the output null, vanilla renaming logic takes over automatically.
    }
}
