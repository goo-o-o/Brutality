package net.goo.brutality.mixin.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.brutality.client.particle.base.ChainLightningParticle;
import net.goo.brutality.client.renderers.BrutalityPhotonParticleRenderType;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Shadow
    @Final
    private RenderBuffers renderBuffers;

    @Inject(
            method = "renderLevel",
            at = @At(
                    shift = At.Shift.AFTER,
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;applyModelViewMatrix()V",
                    ordinal = 0
            )
    )
    private void renderOverlays(PoseStack ps, float partialTicks, long unknown, boolean drawBlockOutline,
                                Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projMat, CallbackInfo ci) {
        ChainLightningParticle.onWorldRenderLast(camera, partialTicks, ps, renderBuffers);
    }


    @Shadow private Frustum cullingFrustum;

    @Shadow @Final private Minecraft minecraft;

    /**
     * inject opaque layer being rendered
     */
    @Inject(
            method = {"renderLevel"},
            at = {@At(
                    value = "CONSTANT",
                    args = {"stringValue=entities"},
                    shift = At.Shift.BEFORE,
                    by = 1
            )}
    )
    private void prepareForParticleRendering(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
        BrutalityPhotonParticleRenderType.prepareForParticleRendering(cullingFrustum);
        MultiBufferSource.BufferSource bufferSource = this.renderBuffers.bufferSource();
        this.minecraft.particleEngine.render(poseStack, bufferSource, lightTexture, camera, partialTick, cullingFrustum);
    }

    /**
     * inject particle being rendered
     */
    @Inject(
            method = {"renderLevel"},
            at = {@At(
                    value = "INVOKE",
                    shift = At.Shift.AFTER,
                    target = "Lnet/minecraft/client/particle/ParticleEngine;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;FLnet/minecraft/client/renderer/culling/Frustum;)V")}
    )
    private void prepareForParticleBloom(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
        BrutalityPhotonParticleRenderType.renderBloom();
    }
}