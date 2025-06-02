package net.goo.armament.client.event;

import net.goo.armament.Armament;
import net.goo.armament.item.base.ArmaGeoItem;
import net.goo.armament.registry.ModItems;
import net.goo.armament.util.helpers.ModTooltipHelper;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static net.goo.armament.util.ModResources.BASE_COLOR_MAP;
import static net.goo.armament.util.ModResources.BETTER_COMBAT_LOADED;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeClientTooltipHandler {

    @SubscribeEvent
    public static void tooltipColorHandler(RenderTooltipEvent.Color event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();

        if (item instanceof ArmaGeoItem) {
            // Create a lazy-loaded map of item classes to their baseColors


            // Find matching baseColors for the item's class
            int[][] baseColors = BASE_COLOR_MAP.get(item.getClass());
            int[][] bgColors = ModTooltipHelper.getBackgroundColors(item.getClass());
            if (baseColors != null && baseColors.length >= 2) {
                // Convert first two color sets to ARGB
                event.setBorderStart(ModTooltipHelper.argbToInt(baseColors[0]));
                event.setBorderEnd(ModTooltipHelper.argbToInt(baseColors[1]));
                event.setBackgroundStart(ModTooltipHelper.argbToInt(bgColors[1], 240));
                event.setBackgroundEnd(ModTooltipHelper.argbToInt(bgColors[0], 225));
            }
        }
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(ModItems.SHADOWSTEP_SWORD.get(),
                    new ResourceLocation("armament", "bettercombat_installed"),
                    (stack, world, entity, seed) -> BETTER_COMBAT_LOADED ? 1.0F : 0.0F);
        });
    }


}
