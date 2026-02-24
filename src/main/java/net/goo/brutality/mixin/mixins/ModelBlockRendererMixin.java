package net.goo.brutality.mixin.mixins;

import net.minecraft.client.renderer.block.ModelBlockRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ModelBlockRenderer.class)
public abstract class ModelBlockRendererMixin {
//
//    @Redirect(
//        method = "putQuadData",
//        at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;putBulkData(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lnet/minecraft/client/renderer/block/model/BakedQuad;[FFFF[IIZ)V")
//    )
//    private void redirectPutBulkData(VertexConsumer instance, PoseStack.Pose pPose, BakedQuad pQuad, float[] pBrightness, float pRed, float pGreen, float pBlue, int[] pLightmap, int pPackedOverlay, boolean pMulColor) {
//        if (ForgeClientPlayerStateHandler.ERROR_404_EQUIPPED && !ModList.get().isLoaded("embeddium")) {
//            // 1. Get the 404 Texture
//            TextureAtlasSprite missing = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
//                    .apply(MissingTextureAtlasSprite.getLocation());
//
//            int[] vertices = pQuad.getVertices();
//            Vec3i normal = pQuad.getDirection().getNormal();
//
//            // 2. Manually write the 4 vertices
//            for (int k = 0; k < 4; k++) {
//                int offset = k * 8;
//                float x = Float.intBitsToFloat(vertices[offset]);
//                float y = Float.intBitsToFloat(vertices[offset + 1]);
//                float z = Float.intBitsToFloat(vertices[offset + 2]);
//
//                // Map UVs to the 4 corners of the missing texture based on index k
//                // Standard quad order: 0 (top-left), 1 (bottom-left), 2 (bottom-right), 3 (top-right)
//                float u = (k == 0 || k == 1) ? missing.getU0() : missing.getU1();
//                float v = (k == 1 || k == 2) ? missing.getV1() : missing.getV0();
//
//                // Build vertex using the standard flow
//                instance.vertex(pPose.pose(), x, y, z)
//                        .color(pRed * pBrightness[k], pGreen * pBrightness[k], pBlue * pBrightness[k], 1.0F)
//                        .uv(u, v)
//                        .overlayCoords(pPackedOverlay)
//                        .uv2(pLightmap[k])
//                        .normal(pPose.normal(), (float)normal.getX(), (float)normal.getY(), (float)normal.getZ())
//                        .endVertex();
//            }
//        } else {
//            // Original behavior
//            instance.putBulkData(pPose, pQuad, pBrightness, pRed, pGreen, pBlue, pLightmap, pPackedOverlay, pMulColor);
//        }
//    }
}