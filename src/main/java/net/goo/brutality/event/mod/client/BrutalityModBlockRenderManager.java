package net.goo.brutality.event.mod.client;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.block.BrutalityGeoBlockEntity;
import net.goo.brutality.client.renderers.block.*;
import net.goo.brutality.client.renderers.layers.BrutalityBlockLayer;
import net.goo.brutality.common.registry.BrutalityBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BrutalityModBlockRenderManager {
    private static final Map<BlockEntity, List<Consumer<BrutalityGeoBlockRenderer<?>>>> LAYER_CONFIGURATIONS = new HashMap<>();

    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BrutalityBlockEntities.TABLE_OF_WIZARDRY_BLOCK_ENTITY.get(), TableOfWizardryRenderer::new);
        event.registerBlockEntityRenderer(BrutalityBlockEntities.PEDESTAL_OF_WIZARDRY_BLOCK_ENTITY.get(), PedestalOfWizardryRenderer::new);
        event.registerBlockEntityRenderer(BrutalityBlockEntities.WHITE_FILING_CABINET_BLOCK_ENTITY.get(), FilingCabinetBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BrutalityBlockEntities.LIGHT_GRAY_FILING_CABINET_BLOCK_ENTITY.get(), FilingCabinetBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BrutalityBlockEntities.GRAY_FILING_CABINET_BLOCK_ENTITY.get(), FilingCabinetBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BrutalityBlockEntities.WATER_COOLER_BLOCK_ENTITY.get(), BrutalityGeoBlockRenderer::new);
        event.registerBlockEntityRenderer(BrutalityBlockEntities.COFFEE_MACHINE_BLOCK_ENTITY.get(), BrutalityGeoBlockRenderer::new);
        event.registerBlockEntityRenderer(BrutalityBlockEntities.SUPER_SNIFFER_FIGURE_BLOCK_ENTITY.get(), BrutalityGeoBlockRenderer::new);
        event.registerBlockEntityRenderer(BrutalityBlockEntities.WET_FLOOR_SIGN_BLOCK_ENTITY.get(), RotatableBlockRenderer::new);
        event.registerBlockEntityRenderer(BrutalityBlockEntities.LCD_MONITOR_BLOCK_ENTITY.get(), RotatableBlockRenderer::new);
        event.registerBlockEntityRenderer(BrutalityBlockEntities.WHITE_OFFICE_CHAIR_BLOCK_ENTITY.get(), RotatableBlockRenderer::new);
        event.registerBlockEntityRenderer(BrutalityBlockEntities.BLACK_OFFICE_CHAIR_BLOCK_ENTITY.get(), RotatableBlockRenderer::new);


    }

    public static IClientItemExtensions getDefaultExtensions() {
        return new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                Minecraft mc = Minecraft.getInstance();
                return BrutalityBlockEntityWithoutLevelRenderer.getInstance(
                        new BlockEntityRendererProvider.Context(
                                mc.getBlockEntityRenderDispatcher(),
                                mc.getBlockRenderer(),
                                mc.getItemRenderer(),
                                mc.getEntityRenderDispatcher(),
                                mc.getEntityModels(),
                                mc.font
                        )
                );
            }
        };
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(FilingCabinetBlockEntityRenderer.FILING_CABINET_LAYER, FilingCabinetBlockEntityRenderer::createBodyLayer);
    }

    public static <T extends BlockEntity & BrutalityGeoBlockEntity> void registerBlockEntityLayers(@NotNull BlockEntity blockEntity, Consumer<BrutalityGeoBlockRenderer<T>> layerConfigurer) {
        LAYER_CONFIGURATIONS.put(blockEntity, List.of((Consumer) layerConfigurer));
    }

    public static <T extends BlockEntity & BrutalityGeoBlockEntity> BrutalityGeoBlockRenderer<T> createRenderer(T block) {
        BrutalityGeoBlockRenderer<T> renderer = new BrutalityGeoBlockRenderer<>();
        renderer.addRenderLayer(new BrutalityBlockLayer<>(renderer));
        List<Consumer<BrutalityGeoBlockRenderer<?>>> configurers = LAYER_CONFIGURATIONS.getOrDefault(block, new ArrayList<>());
        for (Consumer<BrutalityGeoBlockRenderer<?>> configurer : configurers) {
            configurer.accept(renderer);
        }
        return renderer;
    }
}