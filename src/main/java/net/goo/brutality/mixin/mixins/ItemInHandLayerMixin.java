package net.goo.brutality.mixin.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.brutality.client.renderers.shaders.outline.OutlineStyles;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public class ItemInHandLayerMixin {
    @Inject(
            method = "renderArmWithItem",
            at = @At("HEAD")
    )
    private void preRenderArmWithItem(
            LivingEntity pLivingEntity, ItemStack pItemStack, ItemDisplayContext pDisplayContext, HumanoidArm pArm, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci) {
        if (pLivingEntity instanceof Player player) {
            OutlineStyles.push(player, pItemStack);
        }
    }

    @Inject(
            method = "renderArmWithItem",
            at = @At("RETURN")
    )
    private void postRenderArmWithItem(
            LivingEntity pLivingEntity, ItemStack pItemStack, ItemDisplayContext pDisplayContext, HumanoidArm pArm, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci) {
        OutlineStyles.clear();
    }
}