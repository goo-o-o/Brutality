package net.goo.brutality.mixin.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.brutality.client.event.forge.ForgeClientPlayerStateHandler;
import net.goo.brutality.client.renderers.item.Error404BakedModel;
import net.goo.brutality.client.renderers.shaders.outline.OutlineShader;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Inject(method = "getModel", at = @At("RETURN"), cancellable = true)
    private void wrapWith404(ItemStack pStack, @Nullable Level pLevel, @Nullable LivingEntity pEntity, int pSeed, CallbackInfoReturnable<BakedModel> cir) {
        if (ForgeClientPlayerStateHandler.ERROR_404_EQUIPPED) {
            BakedModel originalModel = cir.getReturnValue();

            // Don't wrap if it's already wrapped or if it's null
            if (originalModel != null && !(originalModel instanceof Error404BakedModel)) {
                cir.setReturnValue(new Error404BakedModel(originalModel));
            }
        }
    }

//    @Shadow
//    public abstract void renderModelLists(BakedModel pModel, ItemStack pStack, int pCombinedLight, int pCombinedOverlay, PoseStack pPoseStack, VertexConsumer pBuffer);

//    @Inject(
//            method = "render",
//            at = @At("TAIL")
//    )
//    private void renderOutlineHull(ItemStack stack, ItemDisplayContext context, boolean leftHand, PoseStack ps, MultiBufferSource buffer, int light, int overlay, BakedModel model, CallbackInfo ci) {
//        if (context == ItemDisplayContext.GUI || stack.isEmpty()) return;
//
//        // thickness is in "model units". 0.05f is roughly half a pixel on a 16x texture.
//        ps.pushPose();
//        model.applyTransform(context, ps, leftHand);
//        InvertedHullBakedModel hullModel = new InvertedHullBakedModel(model, 0.05f);

    /// /        ps.mulPose(Axis.ZP.rotationDegrees(180));
    /// /        Vector3f scale = model.getTransforms().getTransform(context).scale;
    /// /        ps.translate(-0.25, -0.25, -0.25);
    /// /        ps.scale(-1.1F, -1.1F, -1.1F);
//        // respect texture alpha (cutout) for standard item sprites
//        VertexConsumer hullConsumer = buffer.getBuffer(RenderType.entityCutout(model.getParticleIcon().atlasLocation()));
//        this.renderModelLists(hullModel, stack, light, overlay, ps, hullConsumer);
//        ps.popPose();
//    }
    @Inject(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderModelLists(Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemStack;IILcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;)V")
    )
    private void renderItemPre(ItemStack stack, ItemDisplayContext context,
                               boolean leftHand, PoseStack ps, MultiBufferSource buffer,
                               int light, int overlay, BakedModel model, CallbackInfo ci) {
        OutlineShader.mixin(context, stack, (((ItemRenderer) (Object) this)), model, light, overlay, ps);
    }
}