package net.goo.armament.client.event;

import net.goo.armament.Armament;
import net.goo.armament.client.entity.ModModelLayers;
import net.goo.armament.client.entity.SwordBeamRenderer;
import net.goo.armament.client.entity.model.CruelSunModel;
import net.goo.armament.client.entity.model.ThrownZeusThunderboltModel;
import net.goo.armament.client.entity.renderer.BlackHoleRenderer;
import net.goo.armament.client.entity.renderer.CruelSunRenderer;
import net.goo.armament.client.entity.renderer.ThrownZeusThunderboltRenderer;
import net.goo.armament.registry.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientHandler {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.THROWN_ZEUS_THUNDERBOLT_ENTITY.get(), ThrownZeusThunderboltRenderer::new);
        event.registerEntityRenderer(ModEntities.CRUEL_SUN_ENTITY.get(), CruelSunRenderer::new);
        event.registerEntityRenderer(ModEntities.TERRA_BEAM.get(), SwordBeamRenderer::new);
        event.registerEntityRenderer(ModEntities.BLACK_HOLE_ENTITY.get(), BlackHoleRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.THROWN_ZEUS_THUNDERBOLT_ENTITY_LAYER, ThrownZeusThunderboltModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.CRUEL_SUN_ENTITY_LAYER, CruelSunModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(Keybindings.DASH_ABILITY_KEY.get());
    }

}