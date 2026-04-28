package net.goo.brutality.client.renderers.physics_bodies;

import com.github.stephengold.joltjni.Quat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.goo.brutality.common.item.base.BrutalityCoinItem;
import net.goo.brutality.common.velthoric.bodies.CoinRigidBody;
import net.goo.brutality.util.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.xmx.velthoric.physics.body.client.VxRenderState;
import net.xmx.velthoric.physics.body.client.body.renderer.VxRigidBodyRenderer;
import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
public class CoinRenderer extends VxRigidBodyRenderer<CoinRigidBody> {
    private final Quaternionf tempQuat = new Quaternionf();

    @Override
    public void render(CoinRigidBody coinRigidBody, PoseStack poseStack, MultiBufferSource multiBufferSource, float partialTick, int packedLight, VxRenderState vxRenderState) {
        ItemStack coinStack = coinRigidBody.getCoinStack();
        if (coinStack == null) return;

        // Calculate alpha once
        int despawnTicks = coinRigidBody.get(CoinRigidBody.DATA_DESPAWN_TICKS);
        float alpha = 1.0f;

        if (despawnTicks >= 0) {
            float progress = Math.min(despawnTicks + partialTick, coinRigidBody.MAX_DESPAWN_TICKS) / coinRigidBody.MAX_DESPAWN_TICKS;
            float rawAlpha = 1.0f - progress;
            alpha = rawAlpha * rawAlpha;
            if (alpha <= 0.005f) return;
        }

        if (!(coinStack.getItem() instanceof BrutalityCoinItem coinItem)) return;

        BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(coinStack, null, null, 0);

        poseStack.pushPose();

        Quat rot = vxRenderState.transform.getRotation();
        tempQuat.set(rot.getX(), rot.getY(), rot.getZ(), rot.getW());
        poseStack.mulPose(tempQuat);

        float scale = coinItem.getPhysicsAndRenderScale(coinRigidBody.getOwner(), coinStack);
        poseStack.scale(scale, scale, scale);

        renderCoinOptimized(coinStack, model, poseStack, multiBufferSource, packedLight, alpha);

        poseStack.popPose();
    }

    private static void renderCoinOptimized(ItemStack coinStack, BakedModel model, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, float alpha) {
        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Axis.XP.rotationDegrees(90));

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        // Optimize GhostBuffer usage
        MultiBufferSource buffer = alpha < 1F ? RenderUtils.makeGhostBuffer(multiBufferSource, alpha) : multiBufferSource;

            itemRenderer.render(coinStack, ItemDisplayContext.NONE, false, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, model);
    }
}