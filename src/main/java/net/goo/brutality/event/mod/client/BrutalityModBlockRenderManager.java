package net.goo.brutality.event.mod.client;

import net.goo.brutality.Brutality;
import net.goo.brutality.block.BrutalityGeoBlockEntity;
import net.goo.brutality.client.renderers.block.BrutalityBlockRenderer;
import net.goo.brutality.client.renderers.layers.BrutalityBlockLayer;
import net.goo.brutality.registry.ModBlockEntities;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BrutalityModBlockRenderManager {
    private static final Map<BlockEntity, List<Consumer<BrutalityBlockRenderer<?>>>> LAYER_CONFIGURATIONS = new HashMap<>();


    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {

        BlockEntityRenderers.register(ModBlockEntities.WATER_COOLER_BLOCK_ENTITY.get(), BrutalityBlockRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.COFFEE_MACHINE_BLOCK_ENTITY.get(), BrutalityBlockRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.SUPER_SNIFFER_FIGURE_BLOCK_ENTITY.get(), BrutalityBlockRenderer::new);

    }

    public static <T extends BlockEntity & BrutalityGeoBlockEntity> void registerBlockEntityLayers(@NotNull BlockEntity blockEntity, Consumer<BrutalityBlockRenderer<T>> layerConfigurer) {
        LAYER_CONFIGURATIONS.put(blockEntity, List.of((Consumer) layerConfigurer));
    }

    public static <T extends BlockEntity & BrutalityGeoBlockEntity> BrutalityBlockRenderer<T> createRenderer(T block) {
        BrutalityBlockRenderer<T> renderer = new BrutalityBlockRenderer<>();
        renderer.addRenderLayer(new BrutalityBlockLayer<>(renderer));
        List<Consumer<BrutalityBlockRenderer<?>>> configurers = LAYER_CONFIGURATIONS.getOrDefault(block, new ArrayList<>());
        for (Consumer<BrutalityBlockRenderer<?>> configurer : configurers) {
            configurer.accept(renderer);
        }
        return renderer;
    }
}