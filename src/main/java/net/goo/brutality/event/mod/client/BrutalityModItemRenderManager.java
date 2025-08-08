package net.goo.brutality.event.mod.client;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.renderers.item.BrutalityItemRenderer;
import net.goo.brutality.client.renderers.layers.*;
import net.goo.brutality.item.base.BrutalityGeoItem;
import net.goo.brutality.registry.BrutalityModItems;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BrutalityModItemRenderManager {
    private static final Map<Item, List<Consumer<BrutalityItemRenderer<?>>>> LAYER_CONFIGURATIONS = new HashMap<>();


    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Register layer configurations for items
            registerItemLayers(BrutalityModItems.FROSTMOURNE_SWORD.get(), renderer ->
                    renderer.addRenderLayer(new AutoGlowingGeoLayer<>(renderer)));

            registerItemLayers(BrutalityModItems.UMBRAL_TOME.get(), renderer ->
                    renderer.addRenderLayer(new BrutalityAutoEndPortalLayer<>(renderer)));
            registerItemLayers(BrutalityModItems.COSMIC_TOME.get(), renderer ->
                    renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer)));
            registerItemLayers(BrutalityModItems.DARKIST_TOME.get(), renderer ->
                    renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer)));
            registerItemLayers(BrutalityModItems.CELESTIA_TOME.get(), renderer ->
                    renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer)));
            registerItemLayers(BrutalityModItems.BRIMWIELDER_TOME.get(), renderer ->
                    renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer)));
            registerItemLayers(BrutalityModItems.VOID_TOME.get(), renderer ->
                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer)));

            registerItemLayers(BrutalityModItems.ATOMIC_JUDGEMENT_HAMMER.get(), renderer ->
                    renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer)));


            registerItemLayers(BrutalityModItems.PROVIDENCE_BOW.get(), renderer -> {
                renderer.addRenderLayer(new AutoGlowingGeoLayer<>(renderer));
                renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer));
            });

            registerItemLayers(BrutalityModItems.CREASE_OF_CREATION_ITEM.get(), renderer ->
                    renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer)));

            registerItemLayers(BrutalityModItems.ATOMIC_JUDGEMENT_HAMMER.get(), renderer ->
                    renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer)));

            registerItemLayers(BrutalityModItems.EVENT_HORIZON_LANCE.get(), renderer ->
                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer)));
            registerItemLayers(BrutalityModItems.DARKIN_SCYTHE.get(), renderer ->
                    renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer)));
            registerItemLayers(BrutalityModItems.BLADE_OF_THE_RUINED_KING.get(), renderer ->
                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer)));
            registerItemLayers(BrutalityModItems.BLADE_OF_THE_RUINED_KING.get(), renderer ->
                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer)));
            registerItemLayers(BrutalityModItems.DOUBLE_DOWN_SWORD.get(), renderer ->
                    renderer.addRenderLayer(new BrutalityAutoAlphaLayer<>(renderer)));
            registerItemLayers(BrutalityModItems.DULL_KNIFE_DAGGER.get(), renderer ->
                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer)));
            registerItemLayers(BrutalityModItems.MURAMASA_SWORD.get(), renderer ->
                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer)));
            registerItemLayers(BrutalityModItems.MURASAMA_SWORD.get(), renderer ->
                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer)));

            registerItemLayers(BrutalityModItems.SEVENTH_STAR_SWORD.get(), renderer ->
                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer)));
            registerItemLayers(BrutalityModItems.VOID_KNIFE_SWORD.get(), renderer ->
                    renderer.addRenderLayer(new BrutalityAutoEndPortalLayer<>(renderer)));
            registerItemLayers(BrutalityModItems.GUNGNIR_TRIDENT.get(), renderer ->
                    renderer.addRenderLayer(new AutoGlowingGeoLayer<>(renderer)));
            registerItemLayers(BrutalityModItems.PANDORAS_CAULDRON.get(), renderer ->
                    renderer.addRenderLayer(new BrutalityAutoEndPortalLayer<>(renderer)));
            registerItemLayers(BrutalityModItems.THUNDERBOLT_TRIDENT.get(), renderer ->
                    renderer.addRenderLayer(new AutoGlowingGeoLayer<>(renderer)));


        });
    }


    public static <T extends Item & BrutalityGeoItem> void registerItemLayers(@NotNull Item item, Consumer<BrutalityItemRenderer<T>> layerConfigurer) {
        LAYER_CONFIGURATIONS.put(item, List.of((Consumer) layerConfigurer));
    }

    public static <T extends Item & BrutalityGeoItem> BrutalityItemRenderer<T> createRenderer(T item) {
        BrutalityItemRenderer<T> renderer = new BrutalityItemRenderer<>();
        renderer.addRenderLayer(new BrutalityItemLayer<>(renderer));
        List<Consumer<BrutalityItemRenderer<?>>> configurers = LAYER_CONFIGURATIONS.getOrDefault(item, new ArrayList<>());
        for (Consumer<BrutalityItemRenderer<?>> configurer : configurers) {
            configurer.accept(renderer);
        }
        return renderer;
    }
}