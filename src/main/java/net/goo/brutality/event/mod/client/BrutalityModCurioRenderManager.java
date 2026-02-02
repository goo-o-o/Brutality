package net.goo.brutality.event.mod.client;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.renderers.curio.BrutalityCurioRenderer;
import net.goo.brutality.client.renderers.layers.BrutalityAutoFullbrightAlphaLayer;
import net.goo.brutality.client.renderers.layers.BrutalityAutoFullbrightLayer;
import net.goo.brutality.common.registry.BrutalityItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BrutalityModCurioRenderManager {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {

        CuriosRendererRegistry.register(BrutalityItems.SERAPHIM_HALO.get(), () ->
                new BrutalityCurioRenderer<>(renderer ->
                        renderer.addRenderLayer(new BrutalityAutoFullbrightAlphaLayer<>(renderer))));
        CuriosRendererRegistry.register(BrutalityItems.FALLEN_ANGELS_HALO.get(), () ->
                new BrutalityCurioRenderer<>(renderer ->
                        renderer.addRenderLayer(new BrutalityAutoFullbrightAlphaLayer<>(renderer))));

        CuriosRendererRegistry.register(BrutalityItems.CELESTIAL_STARBOARD.get(), () ->
                new BrutalityCurioRenderer<>(renderer ->
                        renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer))));


        CuriosRendererRegistry.register(BrutalityItems.WOOLY_BLINDFOLD.get(), BrutalityCurioRenderer::new);
        CuriosRendererRegistry.register(BrutalityItems.SOLAR_SYSTEM.get(), BrutalityCurioRenderer::new);
        CuriosRendererRegistry.register(BrutalityItems.GOLDEN_HEADBAND.get(), BrutalityCurioRenderer::new);
        CuriosRendererRegistry.register(BrutalityItems.TRIAL_GUARDIAN_EYEBROWS.get(), BrutalityCurioRenderer::new);
//        CuriosRendererRegistry.register(BrutalityModItems.SUSPICIOUS_SLOT_MACHINE.get(), SuspiciousSlotMachineRenderer::new);
        CuriosRendererRegistry.register(BrutalityItems.TRIAL_GUARDIAN_HANDS.get(), BrutalityCurioRenderer::new);

        CuriosRendererRegistry.register(BrutalityItems.HELLSPEC_TIE.get(), BrutalityCurioRenderer::new);
        CuriosRendererRegistry.register(BrutalityItems.PROGENITORS_EARRINGS.get(), BrutalityCurioRenderer::new);
        CuriosRendererRegistry.register(BrutalityItems.SANGUINE_SPECTACLES.get(), BrutalityCurioRenderer::new);

    }
}