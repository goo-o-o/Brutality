package net.goo.brutality.mixin.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.brutality.client.particle.base.ChainLightningParticle;
import net.goo.brutality.client.renderers.shaders.PostEffectRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderBuffers;
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
    @Final
    @Shadow
    private Minecraft minecraft;


    @Inject(method = "initOutline()V",
            at = @At("TAIL"))
    private void onInitOutline(CallbackInfo ci) {
        PostEffectRegistry.onInitializeOutline();
    }

    @Inject(method = "resize(II)V",
            at = @At("TAIL"))
    private void onResize(int x, int y, CallbackInfo ci) {
        PostEffectRegistry.resize(x, y);
    }

    @Inject(method = "renderLevel(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lorg/joml/Matrix4f;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/RenderBuffers;bufferSource()Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;",
                    shift = At.Shift.BEFORE
            ))
    private void onRenderLevelBeforeEntities(PoseStack poseStack, float f, long l, boolean b, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo ci) {
        PostEffectRegistry.copyDepth(this.minecraft.getMainRenderTarget());
    }


    @Inject(method = "renderLevel", at = @At("TAIL"))
    private void onLevelRenderEnd(PoseStack poseStack, float partialTicks, long l, boolean b, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo ci) {
        ChainLightningParticle.onWorldRenderLast(camera, partialTicks, poseStack, renderBuffers);

        PostEffectRegistry.processEffects(this.minecraft.getMainRenderTarget(), partialTicks);

        PostEffectRegistry.blitEffects();
    }

}