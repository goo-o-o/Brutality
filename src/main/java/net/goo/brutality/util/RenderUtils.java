package net.goo.brutality.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.brutality.client.BrutalityRenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class RenderUtils {


    public static void renderCylinder(PoseStack poseStack, MultiBufferSource buffer, float radius, float height, int segments) {
        VertexConsumer vc = buffer.getBuffer(BrutalityRenderTypes.GLOW_NO_TEXTURE);

        poseStack.pushPose();
        Matrix4f pose = poseStack.last().pose();

        for (int i = 0; i < segments; i++) {
            float theta1 = (float) (i * Math.PI * 2 / segments);
            float theta2 = (float) ((i + 1) * Math.PI * 2 / segments);

            float x1 = Mth.cos(theta1) * radius;
            float z1 = Mth.sin(theta1) * radius;
            float x2 = Mth.cos(theta2) * radius;
            float z2 = Mth.sin(theta2) * radius;

            float r = 0.04f, g = 0.27f, b = 0.74f, aBottom = 0.9f;
            float aTop = 0.0f;

            vc.vertex(pose, x1, 0, z1).color(r, g, b, aBottom).uv(0, 0).uv2(LightTexture.FULL_BRIGHT).normal(0, 1, 0).endVertex();
            vc.vertex(pose, x2, 0, z2).color(r, g, b, aBottom).uv(0, 0).uv2(LightTexture.FULL_BRIGHT).normal(0, 1, 0).endVertex();
            vc.vertex(pose, x2, height, z2).color(r, g, b, aTop).uv(0, 0).uv2(LightTexture.FULL_BRIGHT).normal(0, 1, 0).endVertex();
            vc.vertex(pose, x1, height, z1).color(r, g, b, aTop).uv(0, 0).uv2(LightTexture.FULL_BRIGHT).normal(0, 1, 0).endVertex();
        }

        poseStack.popPose();
    }


    public static void renderBlockHorizontalGradientSidesLocal(
            PoseStack pPoseStack,
            VertexConsumer pConsumer,
            float height, float width, float depth,
            Color pBottomColor,
            Color pTopColor
    ) {
        pPoseStack.pushPose();
        Matrix4f matrix = pPoseStack.last().pose();

        // Define Normals
        final Vec3 N_NORTH = new Vec3(0, 0, -1);
        final Vec3 N_SOUTH = new Vec3(0, 0, 1);
        final Vec3 N_EAST = new Vec3(1, 0, 0);
        final Vec3 N_WEST = new Vec3(-1, 0, 0);

        float minX = 0.5f - (width / 2.0f);
        float maxX = 0.5f + (width / 2.0f);
        float minZ = 0.5f - (depth / 2.0f);
        float maxZ = 0.5f + (depth / 2.0f);


// --- North Face (Z = minZ plane) ---
        drawSingleBlockSide(pConsumer, matrix, N_NORTH,
                minX, maxX, minZ, minZ,
                height, pBottomColor, pTopColor);

// --- South Face (Z = maxZ plane) ---
        drawSingleBlockSide(pConsumer, matrix, N_SOUTH,
                maxX, minX, maxZ, maxZ, // Swapped X for CCW
                height, pBottomColor, pTopColor);

// --- East Face (X = maxX plane) ---
        drawSingleBlockSide(pConsumer, matrix, N_EAST,
                maxX, maxX, minZ, maxZ,
                height, pBottomColor, pTopColor);

// --- West Face (X = minX plane) ---
        drawSingleBlockSide(pConsumer, matrix, N_WEST,
                minX, minX, maxZ, minZ, // Swapped Z for CCW
                height, pBottomColor, pTopColor);

        pPoseStack.popPose();
    }

    static float r(Color c) {
        return c.getRed() / 255.0f;
    }

    static float g(Color c) {
        return c.getGreen() / 255.0f;
    }

    static float b(Color c) {
        return c.getBlue() / 255.0f;
    }

    static float a(Color c) {
        return c.getAlpha() / 255.0f;
    }

    /**
     * @param pConsumer    The VertexConsumer.
     * @param pMatrix      The current transformation matrix (from pPoseStack.last().pose()).
     * @param pNormal      The normal vector for this face (Vec3).
     * @param x1,          x2 The X range of the quad (either 0.0f/1.0f or 0.0f/0.0f, depending on the face orientation).
     * @param z1,          z2 The Z range of the quad.
     * @param pHeight      The height of the side.
     * @param pBottomColor The color for the vertices at Y=0.
     * @param pTopColor    The color for the vertices at Y=height.
     * @brief Renders a single horizontal block side face (e.g., North, East) in local coordinates.
     */
    private static void drawSingleBlockSide(
            VertexConsumer pConsumer,
            Matrix4f pMatrix,
            Vec3 pNormal,
            float x1, float x2, float z1, float z2,
            float pHeight,
            Color pBottomColor,
            Color pTopColor
    ) {
        // Utility for color component extraction


        // Bottom Color components
        float r_bot = r(pBottomColor), g_bot = g(pBottomColor), b_bot = b(pBottomColor), a_bot = a(pBottomColor);
        // Top Color components
        float r_top = r(pTopColor), g_top = g(pTopColor), b_top = b(pTopColor), a_top = a(pTopColor);

        int fullLight = LightTexture.FULL_BRIGHT;

        // The quad vertices must be defined in Counter-Clockwise (CCW) order
        // looking *outwards* from the block.

        // Corner positions for the face (Y_base = 0.0f, Y_top = pHeight)
        // The order of x1/z1 and x2/z2 must be chosen based on the face and winding order.

        // Vertex 1: Top Left (e.g., T00 for North Face)
        pConsumer.vertex(pMatrix, x1, pHeight, z1).color(r_top, g_top, b_top, a_top).uv(0.0f, 1.0f).uv2(fullLight).normal((float) pNormal.x, (float) pNormal.y, (float) pNormal.z).endVertex();

        // Vertex 2: Top Right (e.g., T10 for North Face)
        pConsumer.vertex(pMatrix, x2, pHeight, z2).color(r_top, g_top, b_top, a_top).uv(1.0f, 1.0f).uv2(fullLight).normal((float) pNormal.x, (float) pNormal.y, (float) pNormal.z).endVertex();

        // Vertex 3: Bottom Right (e.g., P10 for North Face)
        pConsumer.vertex(pMatrix, x2, 0.0f, z2).color(r_bot, g_bot, b_bot, a_bot).uv(1.0f, 0.0f).uv2(fullLight).normal((float) pNormal.x, (float) pNormal.y, (float) pNormal.z).endVertex();

        // Vertex 4: Bottom Left (e.g., P00 for North Face)
        pConsumer.vertex(pMatrix, x1, 0.0f, z1).color(r_bot, g_bot, b_bot, a_bot).uv(0.0f, 0.0f).uv2(fullLight).normal((float) pNormal.x, (float) pNormal.y, (float) pNormal.z).endVertex();
    }
}
