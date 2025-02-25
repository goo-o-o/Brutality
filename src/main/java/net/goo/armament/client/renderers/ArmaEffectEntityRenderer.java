package net.goo.armament.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.goo.armament.client.ArmaEffectEntityModel;
import net.goo.armament.client.renderers.entity.SilencedRotationProjectileRenderer;
import net.goo.armament.entity.helper.ArmaEffectEntity;
import net.goo.armament.entity.helper.ArmaVisualSpecialProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.object.Color;

public class ArmaEffectEntityRenderer extends SilencedRotationProjectileRenderer<ArmaEffectEntity> {

    public ArmaEffectEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ArmaEffectEntityModel());
    }

    @Override
    public void preRender(PoseStack poseStack, ArmaEffectEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        float lerpBodyRot = Mth.rotLerp(partialTick, animatable.yRotO, animatable.getYRot()) - 165;
        float ageInTicks = animatable.tickCount + partialTick;
        applyRotations(animatable, poseStack, ageInTicks, lerpBodyRot, partialTick);
        if (animatable.getVisualType().isRotateRandomly() && !animatable.getVisualType().hasSpecialProperties()) {
            poseStack.mulPose(Axis.XP.rotationDegrees(animatable.getRotationX()));
            poseStack.mulPose(Axis.ZP.rotationDegrees(animatable.getRotationZ()));
        }
        ArmaVisualSpecialProperties.set(animatable, poseStack, partialTick, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void renderFinal(PoseStack poseStack, ArmaEffectEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        super.renderFinal(poseStack, animatable, model, bufferSource, bufferSource.getBuffer(RenderType.entityTranslucentEmissive(getTextureLocation(animatable))), partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, ArmaEffectEntity animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        float f = (float) animatable.getVisualType().getScale();
        super.scaleModelForRender(widthScale * f, heightScale * f, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }

    @Override
    public RenderType getRenderType(ArmaEffectEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture, false);
    }

    @Override
    protected int getBlockLightLevel(ArmaEffectEntity pEntity, BlockPos blockPos) {
        return 15;
    }

    @Override
    protected int getSkyLightLevel(ArmaEffectEntity pEntity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public Color getRenderColor(ArmaEffectEntity animatable, float partialTick, int packedLight) {
        Minecraft mc = Minecraft.getInstance();
        float decreasingAlpha = 1.0f;
        boolean shouldHideAtFirstPerson = false;
        if (animatable.getVisualType().isFadeOut()) {
            int lifespan = animatable.getVisualType().getAnimation().getLifespan();
            decreasingAlpha = 1.0F - ((float) animatable.tickCount / lifespan);
        }

//        if (!CSConfigManager.CLIENT.visibilityOnFirstPerson.get()) {
//            if (mc.level != null && animatable.getOwnerUuid() != null && mc.player != null) {
//                if (mc.options.getCameraType().isFirstPerson() && animatable.getOwnerUuid() == mc.player.getUUID()) {
//                    shouldHideAtFirstPerson = true;
//                }
//            }
//        }
        return Color.ofRGBA(1, 1, 1, shouldHideAtFirstPerson ? 0F : decreasingAlpha);
    }
}