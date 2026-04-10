package net.goo.brutality.client.renderers.layers;

import net.goo.brutality.Brutality;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRenderLayers {

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        // 1. Loop through all registered entity types
        for (EntityType<?> type : ForgeRegistries.ENTITY_TYPES) {
            if (type == EntityType.PLAYER) continue;

            // Cast to LivingEntity type safely
            EntityType<? extends LivingEntity> livingType = (EntityType<? extends LivingEntity>) type;
            LivingEntityRenderer<?, ?> renderer = event.getRenderer(livingType);

            if (renderer != null) {
                addLayerToRenderer(renderer);
            }
        }

        // 3. Handle Players (Skins)
        for (String skinName : event.getSkins()) {
            LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer = event.getSkin(skinName);
            if (renderer != null) {
                renderer.addLayer(new PixelationLayer<>(renderer));
            }
        }
    }

    // This helper method assigns a local name (T and M) to the renderer's wildcards
    private static <T extends LivingEntity, M extends EntityModel<T>> void addLayerToRenderer(LivingEntityRenderer<T, M> renderer) {
        renderer.addLayer(new PixelationLayer<>(renderer));
    }
}
