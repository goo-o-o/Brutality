package net.goo.brutality.mixin.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.brutality.client.event.forge.ForgeClientPlayerStateHandler;
import net.goo.brutality.client.renderers.BrutalityRenderTypes;
import net.goo.brutality.client.renderers.item.Error404BakedModel;
import net.goo.brutality.client.renderers.shaders.BrutalityShaders;
import net.goo.brutality.client.renderers.shaders.PostEffectRegistry;
import net.goo.brutality.client.renderers.shaders.outline.OutlineShader;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.render.ShaderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

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

    @Shadow
    public abstract void renderModelLists(BakedModel pModel, ItemStack pStack, int pCombinedLight, int pCombinedOverlay, PoseStack pPoseStack, VertexConsumer pBuffer);


    @Inject(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderModelLists(Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemStack;IILcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;)V")
    )
    private void renderItemPre(ItemStack stack, ItemDisplayContext context,
                               boolean leftHand, PoseStack poseStack, MultiBufferSource buffer,
                               int light, int overlay, BakedModel model, CallbackInfo ci) {

        boolean isFirstPerson = context == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                || context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
        Player player = isFirstPerson ? Minecraft.getInstance().player : ShaderHelper.getPlayer();

        if (player != null && !isFirstPerson) {
            if (CuriosApi.getCuriosInventory(player)
                    .map(h -> h.isEquipped(BrutalityItems.CENSORSHIP.get()))
                    .orElse(false)) {

                PostEffectRegistry.renderEffectForNextTick(BrutalityShaders.PIXELATED_SHADER);

                VertexConsumer pixelConsumer = buffer.getBuffer(BrutalityRenderTypes.PIXELATE_ITEM);
                this.renderModelLists(model, stack, light, overlay, poseStack, pixelConsumer);
            }
        }

        OutlineShader.mixin(context, stack, (((ItemRenderer) (Object) this)), model, light, overlay, poseStack);
    }
}