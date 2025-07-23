package net.goo.brutality.event.mod.client;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.renderers.curio.BrutalityCurioRenderer;
import net.goo.brutality.client.renderers.layers.BrutalityAutoFullbrightAlphaLayer;
import net.goo.brutality.registry.BrutalityModItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BrutalityModCurioRenderManager {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {

        CuriosRendererRegistry.register(BrutalityModItems.SERAPHIM_HALO.get(), () ->
                new BrutalityCurioRenderer<>(renderer -> {
            renderer.addRenderLayer(new BrutalityAutoFullbrightAlphaLayer<>(renderer));
        }));
        CuriosRendererRegistry.register(BrutalityModItems.CELESTIAL_STARBOARD.get(), () ->
                new BrutalityCurioRenderer<>(renderer -> {
            renderer.addRenderLayer(new BrutalityAutoFullbrightAlphaLayer<>(renderer));
        }));


        CuriosRendererRegistry.register(BrutalityModItems.WOOLY_BLINDFOLD.get(), BrutalityCurioRenderer::new);
        CuriosRendererRegistry.register(BrutalityModItems.SOLAR_SYSTEM.get(), BrutalityCurioRenderer::new);
        CuriosRendererRegistry.register(BrutalityModItems.GOLDEN_HEADBAND.get(), BrutalityCurioRenderer::new);
        CuriosRendererRegistry.register(BrutalityModItems.TRIAL_GUARDIAN_EYEBROWS.get(), BrutalityCurioRenderer::new);
        CuriosRendererRegistry.register(BrutalityModItems.TRIAL_GUARDIAN_HANDS.get(), BrutalityCurioRenderer::new);


    }
}