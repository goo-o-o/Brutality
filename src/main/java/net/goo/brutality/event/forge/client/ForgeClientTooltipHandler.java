package net.goo.brutality.event;

import net.goo.brutality.item.base.BrutalityGeoItem;
import net.goo.brutality.registry.ModRarities;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.goo.brutality.Brutality.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeClientTooltipHandler {

    @SubscribeEvent
    public static void tooltipColorHandler(RenderTooltipEvent.Color event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        Rarity rarity = item.getRarity(stack);

        if (item instanceof BrutalityGeoItem) {
            if (!(rarity.equals(Rarity.COMMON) | rarity.equals(Rarity.UNCOMMON) | rarity.equals(Rarity.RARE) | rarity.equals(Rarity.EPIC))) {
                int[][] colors = ModRarities.getGradientForRarity(rarity).colors;
                event.setBorderStart(BrutalityTooltipHelper.argbToInt(
                        BrutalityTooltipHelper.getCyclingColorFromGradient(0.05f, colors[0], colors[1])));
                event.setBorderEnd(BrutalityTooltipHelper.argbToInt(
                        BrutalityTooltipHelper.getCyclingColorFromGradient(0.05f, colors[1], colors[0])));

            }
        }
    }
}
