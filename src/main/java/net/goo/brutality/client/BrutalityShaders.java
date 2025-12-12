package net.goo.brutality.client;

import com.mojang.blaze3d.shaders.Shader;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.goo.brutality.Brutality;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

import java.io.IOException;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BrutalityShaders {
//    private static ShaderInstance fireShader;
//
//    public static ShaderInstance getFireShader() {
//        return fireShader;
//    }
//
//    @SubscribeEvent
//    public static void registerShaders(RegisterShadersEvent event) throws IOException {
//        registerSafe(event, "fire", s -> BrutalityShaders.fireShader = s);
//    }
//
//    private static void registerSafe(RegisterShadersEvent event, String name, Consumer<ShaderInstance> callback) throws IOException {
//        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, name);
//
//        // This works on ALL 1.20.1 Forge versions (47.0.1 → 47.4.0+)
//        // Shader is now fully compiled and safe to store
//        event.registerShader(
//                new ShaderInstance(event.getResourceProvider(), id, DefaultVertexFormat.POSITION_TEX),
//                callback
//        );
//    }
}