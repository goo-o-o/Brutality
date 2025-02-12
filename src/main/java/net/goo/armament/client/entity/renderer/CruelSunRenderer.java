package net.goo.armament.client.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.goo.armament.Armament;
import net.goo.armament.client.entity.ModModelLayers;
import net.goo.armament.client.entity.model.CruelSunModel;
import net.goo.armament.entity.custom.CruelSunEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class CruelSunRenderer extends EntityRenderer<CruelSunEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Armament.MOD_ID, "textures/entity/cruel_sun.png");
    private final CruelSunModel<CruelSunEntity> model;

    public CruelSunRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new CruelSunModel<>(pContext.bakeLayer(ModModelLayers.CRUEL_SUN_ENTITY_LAYER));
    }

    @Override
    public ResourceLocation getTextureLocation(CruelSunEntity pEntity) {
        return TEXTURE;
    }

    @Override
    public void render(CruelSunEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        // Push the PoseStack to apply transformations
        pPoseStack.pushPose();
        float f1 = (pEntity.tickCount + pPartialTick);
        System.out.println(pEntity.tickCount);

        if (pEntity.tickCount < 20) {
            // Scaling up phase: From 0 to 3
            double x = f1 / 20.0; // Normalize tick count to [0, 1] over 20 ticks
            double scaleFactor = Math.sin(Math.PI / 2 * x); // Ease-in with sine
            pPoseStack.scale((float) scaleFactor * 3F, (float) scaleFactor * 3F, (float) scaleFactor * 3F);
            System.out.println("Scaling Up: " + scaleFactor);
        } else if (pEntity.tickCount < 180) {
            // Waiting phase: Fixed scale of 3
            pPoseStack.scale(3F, 3F, 3F); // No scaling changes during this phase
            System.out.println("Waiting Phase");
        } else {
            // Scaling down phase: From 3 to 0
            double x = (f1 - 180) / 20.0; // Normalize tick count to [0, 1] over 20 ticks
            double scaleFactor = Math.sin(Math.PI / 2 * (1 - x)); // Ease-out with sine
            pPoseStack.scale((float) scaleFactor * 3F, (float) scaleFactor * 3F, (float) scaleFactor * 3F);
            System.out.println("Scaling Down: " + scaleFactor);
        }

        // Optionally: Add rotation or other transformations
        pPoseStack.mulPose(Axis.YP.rotationDegrees(f1 * 3.0F)); // Rotating 3 degrees per tick

        // Apply bobbing effect (optional)
        double bobbingHeight = Math.sin(f1 * 0.1) * 0.25; // Bouncing with 0.5 blocks height
        pPoseStack.translate(0.0D, bobbingHeight - 0.5F, 0.0D);
        VertexConsumer vertexConsumer = pBuffer.getBuffer(RenderType.entityTranslucentEmissive(this.getTextureLocation(pEntity)));
        // Render the model
        this.model.renderToBuffer(pPoseStack, vertexConsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        // Pop the PoseStack to clean up transformations
        pPoseStack.popPose();

        // Call the superclass render method to handle any additional logic
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }


    @Override
    protected int getBlockLightLevel(CruelSunEntity pEntity, BlockPos pPos) {
        return 15;
    }
}
