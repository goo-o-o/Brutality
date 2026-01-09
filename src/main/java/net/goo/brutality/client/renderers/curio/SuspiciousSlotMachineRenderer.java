package net.goo.brutality.client.renderers.curio;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.goo.brutality.item.curios.BrutalityCurioItem;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class SuspiciousSlotMachineRenderer<I extends BrutalityCurioItem> extends BrutalityCurioRenderer<I> {
    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        poseStack.pushPose();

        if (currentEntity instanceof LivingEntity living) {
            // Snap position to the block grid (center of the block)
            double x = Math.floor(living.getX()) + 0.5; // Center x on block
            double y = Math.floor(living.getY()); // Align to block's base
            double z = Math.floor(living.getZ()) + 0.5; // Center z on block

            // Translate to the block's center
            poseStack.translate(x - living.getX(), y - living.getY(), z - living.getZ());

            // Snap rotation to nearest cardinal direction
            float yaw = living.getYRot();
            float mappedYaw = Mth.positiveModulo(yaw, 360.0F);
            float snappedYaw = Math.round(mappedYaw / 90.0F) * 90.0F;

            // Apply rotation to align with cardinal axes
            poseStack.mulPose(Axis.YP.rotationDegrees(-snappedYaw));
        }

        // Render the curio model
        this.prepForRender(slotContext.entity(), stack, EquipmentSlot.HEAD, (HumanoidModel<?>) renderLayerParent.getModel());
        VertexConsumer consumer = renderTypeBuffer.getBuffer(RenderType.armorCutoutNoCull(this.getTextureLocation((I) stack.getItem())));
        this.renderToBuffer(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }
}