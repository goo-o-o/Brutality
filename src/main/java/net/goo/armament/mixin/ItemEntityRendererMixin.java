package net.goo.armament.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.armament.registry.ModItems;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererMixin extends EntityRenderer<ItemEntity> {
    protected ItemEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(
            method = "render(Lnet/minecraft/world/entity/item/ItemEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("HEAD")
    )
    private void beforeRender(ItemEntity entity, float entityYaw, float partialTicks,
                              PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                              CallbackInfo ci) {
        ItemStack stack = entity.getItem();

        if (stack.is(ModItems.TERRA_BLADE.get()) || stack.is(ModItems.SUPERNOVA_SWORD.get())) {
            this.shadowRadius = 0.75F;
        } else {
            this.shadowRadius = 0.15F;
        }
    }
}