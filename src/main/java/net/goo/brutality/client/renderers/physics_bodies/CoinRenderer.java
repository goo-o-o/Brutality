package net.goo.brutality.client.renderers.physics_bodies;

import com.github.stephengold.joltjni.Quat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.goo.brutality.common.velthoric.bodies.CoinRigidBody;
import net.goo.brutality.util.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.xmx.velthoric.physics.body.client.VxRenderState;
import net.xmx.velthoric.physics.body.client.body.renderer.VxRigidBodyRenderer;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
public class CoinRenderer extends VxRigidBodyRenderer<CoinRigidBody> {

    public static void renderCoin(ItemStack coinStack, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, float alpha) {
        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Axis.XP.rotationDegrees(90));

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        itemRenderer.render(coinStack, ItemDisplayContext.NONE, false, poseStack, RenderUtils.makeGhostBuffer(multiBufferSource, alpha), packedLight, OverlayTexture.NO_OVERLAY, itemRenderer.getModel(coinStack, null, null, 0));

        // 3. Render Back (Tail Sticker)
        ResourceLocation tailTexture = getTailTexture(itemRenderer.getModel(coinStack, null, null, 0));
        VertexConsumer consumer = multiBufferSource.getBuffer(RenderType.entityTranslucent(tailTexture));

        // Move to the back.
        // Minecraft item extrusion is 1/16th thick. At 0.5 scale, that's 0.03125.
        // We go slightly further to -0.033 to avoid Z-fighting.
        poseStack.translate(0, 0, 0.0314);

        Matrix4f matrix = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();

        float s = 0.5f;

        // UV FIX: Swap maxU and minU to un-mirror the texture
        float minU = 1f;
        float maxU = 0f;
        float minV = 0f;
        float maxV = 1f;

        // Draw the quad
        // We use a normal of (0, 0, -1) so the back face receives light correctly
        addVertex(matrix, normal, consumer, -s, -s, 0F, maxU, maxV, packedLight, (int) (alpha * 255));
        addVertex(matrix, normal, consumer, s, -s, 0F, minU, maxV, packedLight, (int) (alpha * 255));
        addVertex(matrix, normal, consumer, s, s, 0F, minU, minV, packedLight, (int) (alpha * 255));
        addVertex(matrix, normal, consumer, -s, s, 0F, maxU, minV, packedLight, (int) (alpha * 255));
    }

    @Override
    public void render(CoinRigidBody coinRigidBody, PoseStack poseStack, MultiBufferSource multiBufferSource, float partialTick, int packedLight, VxRenderState vxRenderState) {
        ItemStack coinStack = coinRigidBody.getCoinStack();
        if (coinStack == null) return;

        int despawnTicks = coinRigidBody.get(CoinRigidBody.DATA_DESPAWN_TICKS);
        float alpha = 1.0f;

        if (despawnTicks >= 0) {
            float progress = Math.min(despawnTicks + partialTick, coinRigidBody.MAX_DESPAWN_TICKS) / coinRigidBody.MAX_DESPAWN_TICKS;
            // Linear Alpha
            float rawAlpha = 1.0f - progress;
            // Squared Alpha feels much more "linear" to the human eye in GL rendering
            alpha = rawAlpha * rawAlpha;
        }

        poseStack.pushPose();

        // 1. Rotation & Flattening
        Quat rot = vxRenderState.transform.getRotation();
        poseStack.mulPose(new Quaternionf(rot.getX(), rot.getY(), rot.getZ(), rot.getW()));

        renderCoin(coinStack, poseStack, multiBufferSource, packedLight, alpha);

        poseStack.popPose();
    }

    private static void addVertex(Matrix4f matrix, Matrix3f normal, VertexConsumer consumer, float x, float y, float z, float u, float v, int light, int alpha) {
        consumer.vertex(matrix, x, y, z)
                .color(255, 255, 255, alpha)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, 0, 0, 1.0f) // Pointing OUT away from the back
                .endVertex();
    }

    private static ResourceLocation getTailTexture(BakedModel model) {
        // 2. Get the sprite for the main face (usually the particle icon)
        TextureAtlasSprite baseSprite = model.getParticleIcon();

        // 3. The "Robust" logic:
        // The sprite's name is something like "brutality:item/silver_coin"
        ResourceLocation spriteLoc = baseSprite.contents().name();

        // 4. Append "_tail" to the path
        return ResourceLocation.fromNamespaceAndPath(
                spriteLoc.getNamespace(),
                "textures/" + spriteLoc.getPath() + "_tail.png" // Results in "brutality:item/silver_coin_tail"
        );
    }

}
