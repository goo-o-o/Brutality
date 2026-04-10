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