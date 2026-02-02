package net.goo.brutality.client.renderers;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
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
    private static ShaderInstance fireShader;

    public static ShaderInstance getFireShader() {
        return fireShader;
    }

    private static ShaderInstance manaOrbShader;

    public static ShaderInstance getManaOrbShader() {
        return manaOrbShader;
    }
    private static ShaderInstance bloomShader;

    public static ShaderInstance getBloomShader() {
        return bloomShader;
    }

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException {
        registerSafe(event, "fire", s -> BrutalityShaders.fireShader = s);
        registerSafe(event, "orb", s -> BrutalityShaders.manaOrbShader = s);
//        registerSafe(event, "bloom", s -> BrutalityShaders.bloomShader = s);
    }


    private static void registerSafe(RegisterShadersEvent event, String name, Consumer<ShaderInstance> callback) throws IOException {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, name);
        event.registerShader(
                new ShaderInstance(event.getResourceProvider(), id, DefaultVertexFormat.POSITION_TEX),
                callback
        );
    }
}