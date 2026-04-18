package net.goo.brutality.client.renderers.shaders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.model.BakedQuad;

public class DualVertexConsumer implements VertexConsumer {
    private final VertexConsumer first;
    private final VertexConsumer second;

    public DualVertexConsumer(VertexConsumer first, VertexConsumer second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public VertexConsumer vertex(double x, double y, double z) {
        first.vertex(x, y, z);
        second.vertex(x, y, z);
        return this;
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha) {
        first.color(red, green, blue, alpha);
        second.color(red, green, blue, alpha);
        return this;
    }
    @Override
    public void putBulkData(PoseStack.Pose pPoseEntry, BakedQuad pQuad, float pRed, float pGreen, float pBlue, int pCombinedLight, int pCombinedOverlay) {
        first.putBulkData(pPoseEntry, pQuad, pRed, pGreen, pBlue, pCombinedLight, pCombinedOverlay);
        second.putBulkData(pPoseEntry, pQuad, pRed, pGreen, pBlue, pCombinedLight, pCombinedOverlay);
    }


    @Override
    public VertexConsumer uv(float u, float v) {
        first.uv(u, v);
        second.uv(u, v);
        return this;
    }

    @Override
    public VertexConsumer overlayCoords(int u, int v) {
        first.overlayCoords(u, v);
        second.overlayCoords(u, v);
        return this;
    }

    @Override
    public VertexConsumer uv2(int u, int v) {
        first.uv2(u, v);
        second.uv2(u, v);
        return this;
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        first.normal(x, y, z);
        second.normal(x, y, z);
        return this;
    }

    @Override
    public void endVertex() {
        first.endVertex();
        second.endVertex();
    }

    @Override
    public void vertex(float pX, float pY, float pZ, float pRed, float pGreen, float pBlue, float pAlpha, float pTexU, float pTexV, int pOverlayUV, int pLightmapUV, float pNormalX, float pNormalY, float pNormalZ) {
        first.vertex(pX, pY, pZ, pRed, pGreen, pBlue, pAlpha, pTexU, pTexV, pOverlayUV, pLightmapUV, pNormalX, pNormalY, pNormalZ);
        second.vertex(pX, pY, pZ, pRed, pGreen, pBlue, pAlpha, pTexU, pTexV, pOverlayUV, pLightmapUV, pNormalX, pNormalY, pNormalZ);
    }

    @Override
    public void defaultColor(int pDefaultR, int pDefaultG, int pDefaultB, int pDefaultA) {
        first.defaultColor(pDefaultR, pDefaultG, pDefaultB, pDefaultA);
        second.defaultColor(pDefaultR, pDefaultG, pDefaultB, pDefaultA);
    }

    @Override
    public void unsetDefaultColor() {
        first.unsetDefaultColor();
        second.unsetDefaultColor();
    }
}