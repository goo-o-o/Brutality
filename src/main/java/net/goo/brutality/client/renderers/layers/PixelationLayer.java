package net.goo.brutality.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.brutality.client.renderers.BrutalityRenderTypes;
import net.goo.brutality.client.renderers.shaders.BrutalityShaders;
import net.goo.brutality.client.renderers.shaders.PostEffectRegistry;
import net.goo.brutality.common.registry.BrutalityItems;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.curios.api.CuriosApi;

public class PixelationLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public PixelationLayer(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (CuriosApi.getCuriosInventory(entity).map(h -> h.isEquipped(BrutalityItems.CENSORED.get())).orElse(false)) {
            PostEffectRegistry.renderEffectForNextTick(BrutalityShaders.PIXELATED_SHADER);
            VertexConsumer consumer = buffer.getBuffer(BrutalityRenderTypes.PIXELATE);
            poseStack.pushPose();
            poseStack.scale(1.25F, 1.25F, 1.25F);
            this.getParentModel().renderToBuffer(poseStack, consumer, packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0), 1, 1, 1, 1);
            poseStack.popPose();
        }
    }
}