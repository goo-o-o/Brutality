package net.goo.brutality.client.event.forge;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.base.BrutalityMagicItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeClientTooltipEvents {


    // Move tooltipImage for magic items to the bottom
    @SubscribeEvent
    public static void onGatherComponents(RenderTooltipEvent.GatherComponents event) {
        BrutalityMagicItem.renderAugmentSlots(event);


    }
}
