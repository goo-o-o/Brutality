package net.goo.brutality.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.brutality.item.base.BrutalityGeoItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
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
        Item item = entity.getItem().getItem();

        if (item instanceof BrutalityGeoItem brutalityGeoItem) {
            this.shadowRadius = brutalityGeoItem.shadowSize();
        } else {
            this.shadowRadius = 0.15F;
        }
    }
}