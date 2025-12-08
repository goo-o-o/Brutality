package net.goo.brutality.client;

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

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BrutalityShaders {
    private static ShaderInstance fireShader;
    private static ShaderInstance foilShader;
    private static final long startTime = System.currentTimeMillis();
    public static ShaderInstance getFireShader() {
        return fireShader;
    }
    public static ShaderInstance getFoilShader() {
        return foilShader;
    }

    public static void applyFoilToTooltip(GuiGraphics guiGraphics, int x, int y, int z, int width, int height) {
        if (foilShader == null) return;

        // Save current state
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
//        RenderSystem.enableDepthTest();

        // Set up the shader
        RenderSystem.setShader(BrutalityShaders::getFoilShader);

        // Apply shader uniforms
        float time = (System.currentTimeMillis() - startTime) / 1000f;
        foilShader.safeGetUniform("Time").set(time);
        foilShader.safeGetUniform("Intensity").set(1.0F);
        foilShader.safeGetUniform("Dissolve").set(0.0F); // No dissolve for tooltips usually
        foilShader.safeGetUniform("BurnColor1").set(1.0F, 0.5F, 0.0F, 1.0F);
        foilShader.safeGetUniform("BurnColor2").set(0.0F, 1.0F, 0.5F, 1.0F);

        // Draw tooltip area with shader
        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        pose.translate(0, 0, z + 5); // Ensure it renders on top

        // Create a texture for the tooltip area
        RenderSystem.setShaderTexture(0, Screen.BACKGROUND_LOCATION);

        BufferBuilder builder = Tesselator.getInstance().getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        Matrix4f matrix = pose.last().pose();
        float u0 = 0.0f;
        float u1 = 1.0f;
        float v0 = 0.0f;
        float v1 = 1.0f;

        builder.vertex(matrix, x, y + height, 0).uv(u0, v1).color(255, 255, 255, 255).endVertex();
        builder.vertex(matrix, x + width, y + height, 0).uv(u1, v1).color(255, 255, 255, 255).endVertex();
        builder.vertex(matrix, x + width, y, 0).uv(u1, v0).color(255, 255, 255, 255).endVertex();
        builder.vertex(matrix, x, y, 0).uv(u0, v0).color(255, 255, 255, 255).endVertex();

        BufferUploader.drawWithShader(builder.end());

        pose.popPose();

        // Restore state
        RenderSystem.disableDepthTest();
    }

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(
                event.getResourceProvider(),
                ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "fire"),
                DefaultVertexFormat.POSITION_TEX
        ), shader -> fireShader = shader);

        event.registerShader(new ShaderInstance(
                event.getResourceProvider(),
                ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "foil"),
                DefaultVertexFormat.POSITION_TEX
        ), shader -> foilShader = shader);
    }
}