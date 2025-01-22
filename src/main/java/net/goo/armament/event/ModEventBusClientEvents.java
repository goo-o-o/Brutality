package net.goo.armament.event;

import net.goo.armament.Armament;
import net.goo.armament.entity.ModEntities;
import net.goo.armament.entity.client.ModModelLayers;
import net.goo.armament.entity.client.model.CruelSunModel;
import net.goo.armament.entity.client.model.ThrownZeusThunderboltModel;
import net.goo.armament.entity.client.renderer.CruelSunRenderer;
import net.goo.armament.entity.client.renderer.ThrownZeusThunderboltRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.THROWN_ZEUS_THUNDERBOLT_ENTITY.get(), ThrownZeusThunderboltRenderer::new);
        event.registerEntityRenderer(ModEntities.CRUEL_SUN_ENTITY.get(), CruelSunRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.THROWN_ZEUS_THUNDERBOLT_ENTITY_LAYER, ThrownZeusThunderboltModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.CRUEL_SUN_ENTITY_LAYER, CruelSunModel::createBodyLayer);
    }
}