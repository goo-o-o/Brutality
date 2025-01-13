package net.goo.arma.event;

import net.goo.arma.Arma;
import net.goo.arma.entity.client.ModModelLayers;
import net.goo.arma.entity.client.SupernovaExplosionModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Arma.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.SUPERNOVA_EXPLOSION_LAYER, SupernovaExplosionModel::createBodyLayer);

    }

}
