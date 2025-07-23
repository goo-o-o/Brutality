//package net.goo.brutality.event.mod.client;
//
//import net.goo.brutality.Brutality;
//import net.goo.brutality.client.renderers.item.BrutalityItemRenderer;
//import net.goo.brutality.client.renderers.layers.BrutalityAutoFullbrightNoDepthLayer;
//import net.goo.brutality.client.renderers.layers.BrutalityItemLayer;
//import net.goo.brutality.item.base.BrutalityGeoItem;
//import net.goo.brutality.registry.BrutalityModItems;
//import net.minecraft.world.item.Item;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
//import org.jetbrains.annotations.NotNull;
//import software.bernie.geckolib.animatable.GeoItem;
//import software.bernie.geckolib.renderer.GeoItemRenderer;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.function.Consumer;
//
//@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
//public class BrutalityItemRenderManager {
//    private static final Map<Item, Consumer<BrutalityItemRenderer<?>>> RENDERER_CONFIGS = new LinkedHashMap<>();
//
//    public static <T extends Item & BrutalityGeoItem> void registerRendererConfig(@NotNull Item item, Consumer<BrutalityItemRenderer<T>> config) {
//        RENDERER_CONFIGS.put(item, renderer -> config.accept((BrutalityItemRenderer<T>) renderer));
//    }
//
//    @SubscribeEvent
//    public static void onClientSetup(FMLClientSetupEvent event) {
//        event.enqueueWork(() -> {
//            // Register all configured renderers
//            RENDERER_CONFIGS.forEach((item, config) -> {
//                GeoItemRenderer(item, () -> {
//                    BrutalityItemRenderer<?> renderer = new BrutalityItemRenderer<>();
//                    config.accept(renderer);
//                    return renderer;
//                });
//            });
//        });
//    }
//
//    static {
//        registerRendererConfig(
//                BrutalityModItems.EVENT_HORIZON_LANCE.get(),
//                renderer -> {
//                    renderer.addRenderLayer(new BrutalityItemLayer<>(renderer));
//                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer));
//                }
//        );
//    }
//}