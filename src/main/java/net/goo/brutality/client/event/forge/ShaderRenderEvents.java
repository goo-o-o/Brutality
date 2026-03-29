package net.goo.brutality.client.event.forge;

import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.goo.brutality.client.renderers.BrutalityRenderTypes;
import net.goo.brutality.client.renderers.shaders.PostShaderInstance;
import net.goo.brutality.client.renderers.shaders.PostShaderManager;
import net.goo.brutality.util.render.ShaderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ShaderRenderEvents {
    public static final Map<PostShaderInstance, RenderTarget> silhouetteTargets = new HashMap<>();
    public static final Map<PostShaderInstance, MultiBufferSource.BufferSource> handBuffers = new HashMap<>();
    public static final Map<PostShaderInstance, MultiBufferSource.BufferSource> worldBuffers = new HashMap<>();
    public static RenderTarget getSilhouetteTarget(PostShaderInstance shader) {
        return silhouetteTargets.computeIfAbsent(shader, k -> {
            RenderTarget t = new MainTarget(
                    Minecraft.getInstance().getMainRenderTarget().width,
                    Minecraft.getInstance().getMainRenderTarget().height
            );
            t.setClearColor(0, 0, 0, 0);
            t.clear(Minecraft.ON_OSX);
            return t;
        });
    }
    public static MultiBufferSource.BufferSource getHandBuffer(PostShaderInstance shader) {
        return handBuffers.computeIfAbsent(shader, k -> {
            Map<RenderType, BufferBuilder> buffers = new Object2ObjectLinkedOpenHashMap<>();
            buffers.put(BrutalityRenderTypes.ITEM_OUTLINE, new BufferBuilder(BrutalityRenderTypes.ITEM_OUTLINE.bufferSize));
            return MultiBufferSource.immediateWithBuffers(buffers, new BufferBuilder(256));
        });
    }
    public static MultiBufferSource.BufferSource getWorldBuffer(PostShaderInstance shader) {
        return worldBuffers.computeIfAbsent(shader, k -> {
            Map<RenderType, BufferBuilder> buffers = new Object2ObjectLinkedOpenHashMap<>();
            buffers.put(BrutalityRenderTypes.ITEM_OUTLINE, new BufferBuilder(BrutalityRenderTypes.ITEM_OUTLINE.bufferSize));
            return MultiBufferSource.immediateWithBuffers(buffers, new BufferBuilder(256));
        });
    }

    // World matrix captured at AFTER_PARTICLES for world-space items
    public static Matrix4f savedWorldModelViewMat;
    public static float savedFogStart;

    // Hand matrices captured in the Mixin when first-person item renders
    public static Matrix4f capturedFirstPersonHandModelViewMat;
    public static Matrix4f capturedFirstPersonHandProjMat;

    // Thickness set by Mixin from the OutlineStyle
    public static int currentThickness = 2;

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            savedWorldModelViewMat = new Matrix4f(RenderSystem.getModelViewMatrix());
            PostShaderManager.viewStackMatrix = savedWorldModelViewMat;
            savedFogStart = RenderSystem.getShaderFogStart();
        }

        if (ShaderHelper.shouldUseAlternateRendering()) {
            if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
                doEffectRendering();
            }
        } else {
            if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER) {
                doEffectRendering();
            }
        }
    }
    private static void doEffectRendering() {
        Minecraft mc = Minecraft.getInstance();
        RenderTarget mainTarget = mc.getMainRenderTarget();

        // Collect which shaders have pending silhouettes
        Set<PostShaderInstance> activeShaders = new HashSet<>();
        activeShaders.addAll(pendingWorldShaders);
        activeShaders.addAll(pendingHandShaders);

        for (PostShaderInstance shader : activeShaders) {
            RenderTarget target = getSilhouetteTarget(shader);
            target.clear(Minecraft.ON_OSX);
            target.copyDepthFrom(mainTarget);
            target.bindWrite(false);

            PoseStack mvStack = RenderSystem.getModelViewStack();

            // Flush world buffer for this shader
            if (savedWorldModelViewMat != null && pendingWorldShaders.contains(shader)) {
                mvStack.pushPose();
                mvStack.last().pose().set(savedWorldModelViewMat);
                mvStack.last().normal().set(new Matrix3f(savedWorldModelViewMat).invert().transpose());
                RenderSystem.applyModelViewMatrix();

                getWorldBuffer(shader).endBatch(BrutalityRenderTypes.ITEM_OUTLINE);

                mvStack.popPose();
                RenderSystem.applyModelViewMatrix();
            }

            // Flush hand buffer for this shader
            if (capturedFirstPersonHandModelViewMat != null && pendingHandShaders.contains(shader)) {
                mvStack.pushPose();
                mvStack.last().pose().set(capturedFirstPersonHandModelViewMat);
                mvStack.last().normal().set(new Matrix3f(capturedFirstPersonHandModelViewMat).invert().transpose());
                RenderSystem.applyModelViewMatrix();
                if (capturedFirstPersonHandProjMat != null)
                    RenderSystem.setProjectionMatrix(capturedFirstPersonHandProjMat, VertexSorting.DISTANCE_TO_ORIGIN);
                getHandBuffer(shader).endBatch(BrutalityRenderTypes.ITEM_OUTLINE);
                mvStack.popPose();
                RenderSystem.applyModelViewMatrix();
                Matrix4f worldProj = mc.gameRenderer.getProjectionMatrix(
                        mc.gameRenderer.getFov(mc.gameRenderer.getMainCamera(), mc.getPartialTick(), true));
                RenderSystem.setProjectionMatrix(worldProj, VertexSorting.DISTANCE_TO_ORIGIN);
            }

            mainTarget.bindWrite(false);

            // Run this shader's post process
            shader.process();
        }

        capturedFirstPersonHandModelViewMat = null;
        capturedFirstPersonHandProjMat = null;
        pendingWorldShaders.clear();
        pendingHandShaders.clear();
    }

    public static final Set<PostShaderInstance> pendingWorldShaders = new HashSet<>();
    public static final Set<PostShaderInstance> pendingHandShaders = new HashSet<>();


    public static void resizeAllTargets(int width, int height) {
        for (RenderTarget target : silhouetteTargets.values()) {
            target.resize(width, height, Minecraft.ON_OSX);
        }
    }
}