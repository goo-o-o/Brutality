package net.goo.armament.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import org.joml.Matrix4f;

public class SphereRenderer {
    private static final int SEGMENTS = 32; // Increase for smoother sphere
    private static final int RINGS = 16;

    public static void renderSphere(PoseStack poseStack, MultiBufferSource buffer, float radius, float r, float g, float b, float alpha, int light) {
        VertexConsumer vertexBuilder = buffer.getBuffer(RenderType.entityTranslucentCull(TextureAtlas.LOCATION_BLOCKS));
        Matrix4f matrix = poseStack.last().pose();

        // Generate sphere vertices
        for (int i = 0; i < RINGS; i++) {
            double lat0 = Math.PI * (-0.5 + (double) (i - 1) / RINGS);
            double z0 = Math.sin(lat0);
            double zr0 = Math.cos(lat0);

            double lat1 = Math.PI * (-0.5 + (double) i / RINGS);
            double z1 = Math.sin(lat1);
            double zr1 = Math.cos(lat1);

            for (int j = 0; j <= SEGMENTS; j++) {
                double lng = 2 * Math.PI * (double) (j - 1) / SEGMENTS;
                double x = Math.cos(lng);
                double y = Math.sin(lng);

                // Normal vector (for lighting)
                float nx = (float) (x * zr0);
                float ny = (float) (y * zr0);
                float nz = (float) z0;

                vertexBuilder.vertex(matrix,
                                (float)(x * zr0 * radius),
                                (float)(z0 * radius),
                                (float)(y * zr0 * radius))
                        .color(r, g, b, alpha)
                        .uv((float)j/SEGMENTS, (float)(i-1)/RINGS)
                        .uv2(light)
                        .normal(nx, ny, nz)
                        .endVertex();

                nx = (float) (x * zr1);
                ny = (float) (y * zr1);
                nz = (float) z1;

                vertexBuilder.vertex(matrix,
                                (float)(x * zr1 * radius),
                                (float)(z1 * radius),
                                (float)(y * zr1 * radius))
                        .color(r, g, b, alpha)
                        .uv((float)j/SEGMENTS, (float)i/RINGS)
                        .uv2(light)
                        .normal(nx, ny, nz)
                        .endVertex();
            }
        }
    }
}