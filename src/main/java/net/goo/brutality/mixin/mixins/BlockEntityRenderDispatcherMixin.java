package net.goo.brutality.mixin.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.brutality.client.event.forge.ForgeClientPlayerStateHandler;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderDispatcherMixin {



    @Inject(method = "setupAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/blockentity/BlockEntityRenderer;render(Lnet/minecraft/world/level/block/entity/BlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V"))
    private static <T extends BlockEntity> void apply404State(BlockEntityRenderer<T> pRenderer, T pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, CallbackInfo ci) {
        if (ForgeClientPlayerStateHandler.ERROR_404_EQUIPPED) {
            // 1. Force the GPU to use the Missing Texture for whatever is about to be drawn
            RenderSystem.setShaderTexture(0, MissingTextureAtlasSprite.getLocation());

            // 2. Fix the "Solid Color" by resetting/scaling the texture matrix
            RenderSystem.setTextureMatrix(new org.joml.Matrix4f().identity().scale(16.0f));
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    private <T extends BlockEntity> void clear404State(CallbackInfo ci) {
        if (ForgeClientPlayerStateHandler.ERROR_404_EQUIPPED) {
            RenderSystem.setTextureMatrix(new org.joml.Matrix4f().identity());
        }
    }
}
