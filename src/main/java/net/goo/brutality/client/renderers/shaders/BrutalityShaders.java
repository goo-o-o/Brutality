package net.goo.brutality.client.renderers.shaders;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.goo.brutality.Brutality;
import net.goo.brutality.client.renderers.shaders.outline.MaxSwordOutlineShader;
import net.goo.brutality.client.renderers.shaders.outline.OutlineShader;
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
    private static ShaderInstance fireShader;

    private static ShaderInstance itemOutlineCoreShader;
    public static ShaderInstance getItemOutlineCoreShader() {
        return itemOutlineCoreShader;
    }
    public static ShaderInstance getParticleOutlineCoreShader() {
        return itemOutlineCoreShader;
    }
    private static ShaderInstance particleOutlineCoreShader;

    public static ShaderInstance getFireShader() {
        return fireShader;
    }

    private static ShaderInstance manaOrbShader;

    public static ShaderInstance getManaOrbShader() {
        return manaOrbShader;
    }

    public static PostShaderInstance itemOutlinePostShader;
    public static PostShaderInstance maxSwordOutlinePostShader;

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException {
        registerSafe(event, "fire", s -> BrutalityShaders.fireShader = s, DefaultVertexFormat.POSITION_TEX);
        registerSafe(event, "orb", s -> BrutalityShaders.manaOrbShader = s, DefaultVertexFormat.POSITION_TEX);
        registerSafe(event, "item_outline", s -> BrutalityShaders.itemOutlineCoreShader = s, DefaultVertexFormat.BLOCK);
        registerSafe(event, "particle_outline", s -> BrutalityShaders.particleOutlineCoreShader = s, DefaultVertexFormat.POSITION);

        itemOutlinePostShader = new OutlineShader();
        itemOutlinePostShader.setActive(true);
        maxSwordOutlinePostShader = new MaxSwordOutlineShader();
        maxSwordOutlinePostShader.setActive(true);
    }


    private static void registerSafe(RegisterShadersEvent event, String name, Consumer<ShaderInstance> callback, VertexFormat vertexFormat) throws IOException {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, name);
        event.registerShader(
                new ShaderInstance(event.getResourceProvider(), id, vertexFormat),
                callback
        );
    }


}