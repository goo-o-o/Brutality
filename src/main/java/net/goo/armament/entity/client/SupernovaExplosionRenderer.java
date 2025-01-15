package net.goo.armament.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.armament.Armament;
import net.goo.armament.entity.custom.SupernovaExplosionEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SupernovaExplosionRenderer extends EntityRenderer<SupernovaExplosionEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Armament.MOD_ID, "textures/entity/supernova_explosion.png");
    private final SupernovaExplosionModel model;

    public SupernovaExplosionRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SupernovaExplosionModel(context.bakeLayer(ModModelLayers.SUPERNOVA_EXPLOSION_LAYER));
    }

    @Override
    public void render(SupernovaExplosionEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        // Positioning, scaling, or rotation adjustments (if needed)
        poseStack.translate(0, entity.getBbHeight() / 2.0, 0); // Center it
        poseStack.scale(1.0F, 1.0F, 1.0F); // Scale appropriately

        // Render the model
        this.model.renderToBuffer(poseStack, bufferSource.getBuffer(this.model.renderType(TEXTURE)), packedLight, 1, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(SupernovaExplosionEntity entity) {
        return TEXTURE;
    }
}
