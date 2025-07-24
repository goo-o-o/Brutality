//package net.goo.brutality.event.mod.client;
//
//import net.goo.brutality.Brutality;
//import net.goo.brutality.client.renderers.block.BrutalityBlockRenderer;
//import net.goo.brutality.registry.ModBlockEntities;
//import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
//
//@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
//public class BrutalityModBlockRenderManager {
//
//    @SubscribeEvent
//    public static void onClientSetup(FMLClientSetupEvent event) {
//
//        BlockEntityRenderers.register(ModBlockEntities.WATER_COOLER_BLOCK_ENTITY.get(), BrutalityBlockRenderer::new);
//        BlockEntityRenderers.register(ModBlockEntities.COFFEE_MACHINE_BLOCK_ENTITY.get(), BrutalityBlockRenderer::new);
//
//    }
//}