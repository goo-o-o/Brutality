package net.goo.brutality.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.goo.brutality.client.models.IRandomModelPart;
import net.goo.brutality.common.entity.spells.celestia.LightBinding;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.registry.BrutalityEntities;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class LightBoundLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final EntityRenderDispatcher dispatcher;

    public LightBoundLayer(EntityRendererProvider.Context pContext, LivingEntityRenderer<T, M> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = pContext.getEntityRenderDispatcher();
    }

    protected void renderStuckItem(PoseStack poseStack, MultiBufferSource multiBufferSource, int pPackedLight, Entity entity, float pPartialTick, float rotationOffset, float spinAngle) {
        LightBinding lightBinding = new LightBinding(BrutalityEntities.LIGHT_BINDING.get(), entity.level());
        // Apply X shape rotation
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotationOffset));
        // Apply revolution around Y-axis (like a spinning top)
        poseStack.mulPose(Axis.YP.rotationDegrees(spinAngle));
        this.dispatcher.render(lightBinding, 0.0D, 0.0D, 0.0D, 0.0F, pPartialTick, poseStack, multiBufferSource, pPackedLight);
    }

    public void render(@NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, @NotNull T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (pLivingEntity.hasEffect(BrutalityEffects.LIGHT_BOUND.get()))
            if (this.getParentModel() instanceof IRandomModelPart) {
                float scale = Math.max(pLivingEntity.getBbWidth(), pLivingEntity.getBbHeight()) * 0.5F;

                for (int j = 0; j < 2; ++j) {
                    pPoseStack.pushPose();
                    // Move to entity center (half the bounding box height)
                    pPoseStack.translate(0.0F, (3 / pLivingEntity.getBbHeight()) * 0.25F, 0.0F);
                    // Scale based on entity size
                    pPoseStack.scale(scale, scale, scale);
                    // Rotate to form X shape (45 degrees left or right)
                    float rotationOffset = j == 0 ? 45.0F : -45.0F;
                    // Calculate spin angle for revolution
                    float spinAngle = (pAgeInTicks * 5) % 360.0F;
                    this.renderStuckItem(pPoseStack, pBuffer, pPackedLight, pLivingEntity, pPartialTick, rotationOffset, spinAngle);
                    pPoseStack.popPose();
                }
            }
    }
}
