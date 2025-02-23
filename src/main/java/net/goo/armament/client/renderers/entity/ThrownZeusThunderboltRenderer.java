package net.goo.armament.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.goo.armament.Armament;
import net.goo.armament.client.entity.ModModelLayers;
import net.goo.armament.client.entity.model.ThrownZeusThunderboltModel;
import net.goo.armament.entity.custom.ThrownThunderbolt;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ThrownZeusThunderboltRenderer extends EntityRenderer<ThrownThunderbolt> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Armament.MOD_ID, "textures/entity/thrown_zeus_thunderbolt.png");
    private final ThrownZeusThunderboltModel model;

    public ThrownZeusThunderboltRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new ThrownZeusThunderboltModel(pContext.bakeLayer(ModModelLayers.THROWN_ZEUS_THUNDERBOLT_ENTITY_LAYER));
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownThunderbolt pEntity) {
        return TEXTURE;
    }

    public void render(ThrownThunderbolt pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot()) - 90.0F));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot()) + 90.0F));
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(pBuffer, this.model.renderType(this.getTextureLocation(pEntity)), false, pEntity.isFoil());
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    protected int getBlockLightLevel(ThrownThunderbolt pEntity, BlockPos pPos) {
        return 15;
    }

}