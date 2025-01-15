package net.goo.armament.event;

import net.goo.armament.Armament;
import net.goo.armament.entity.client.ModModelLayers;
import net.goo.armament.entity.client.SupernovaExplosionModel;
import net.goo.armament.entity.client.TestTridentModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.SUPERNOVA_EXPLOSION_LAYER, SupernovaExplosionModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.TEST_TRIDENT_LAYER, TestTridentModel::createBodyLayer);

    }

}
