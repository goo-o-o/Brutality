package net.goo.brutality.client.renderers;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.goo.brutality.Brutality;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BrutalityShaders {
    private static ShaderInstance particleShader;
    public static ShaderInstance getParticleShader() {
        return particleShader;
    }

    private static ShaderInstance blitShader;
    public static ShaderInstance getBlitShader() {
        return blitShader;
    }

    private static ShaderInstance fireShader;
    public static ShaderInstance getFireShader() {
        return fireShader;
    }

    private static ShaderInstance manaOrbShader;
    public static ShaderInstance getManaOrbShader() {
        return manaOrbShader;
    }

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException {
        registerSafe(event, "fire", s -> BrutalityShaders.fireShader = s, DefaultVertexFormat.POSITION_TEX);
        registerSafe(event, "orb", s -> BrutalityShaders.manaOrbShader = s, DefaultVertexFormat.POSITION_TEX);
//        registerSafe(event, "bloom", s -> BrutalityShaders.bloomShader = s);
        registerSafe(event, "ld_particle", s -> BrutalityShaders.particleShader = s, DefaultVertexFormat.PARTICLE);
        registerSafe(event, "ld_fast_blit", s -> BrutalityShaders.blitShader = s, DefaultVertexFormat.POSITION);
    }


    private static void registerSafe(RegisterShadersEvent event, String name, Consumer<ShaderInstance> callback, VertexFormat vertexFormat) throws IOException {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, name);
        event.registerShader(
                new ShaderInstance(event.getResourceProvider(), id, vertexFormat),
                callback
        );
    }


}