package net.goo.brutality.mixin.mixins;

import com.lowdragmc.lowdraglib.client.scene.CameraEntity;
import com.lowdragmc.lowdraglib.client.scene.ISceneBlockRenderHook;
import com.lowdragmc.lowdraglib.client.scene.ParticleManager;
import com.lowdragmc.lowdraglib.client.scene.WorldSceneRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.brutality.client.renderers.BrutalityPhotonParticleRenderType;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Collection;

@Mixin(WorldSceneRenderer.class)
public class WorldSceneRendererMixin {
    @Shadow(remap = false) protected ParticleManager particleManager;
    @Shadow(remap = false) protected CameraEntity cameraEntity;

    @Shadow(remap = false) protected Camera camera;

    @Inject(
            method = {"renderTESR"},
            at = {@At(value = "RETURN")},
            remap = false
    )
    private void injectRenderTESR(Collection<BlockPos> poses, PoseStack pose, MultiBufferSource.BufferSource buffers, @Nullable ISceneBlockRenderHook hook, float partialTicks, CallbackInfo ci) {
        if (this.particleManager != null) {
            BrutalityPhotonParticleRenderType.prepareForParticleRendering(null);
            PoseStack poseStack = new PoseStack();
            poseStack.setIdentity();
            poseStack.translate(this.cameraEntity.getX(), this.cameraEntity.getY(), this.cameraEntity.getZ());
            particleManager.render(poseStack, this.camera, partialTicks);
        }
    }
}
