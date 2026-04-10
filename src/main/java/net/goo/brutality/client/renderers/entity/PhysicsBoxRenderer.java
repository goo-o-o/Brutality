package net.goo.brutality.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.entity.base.phys.PhysicsEntity;
import net.goo.brutality.util.math.phys.hitboxes.OrientedBoundingBox;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class PhysicsBoxRenderer extends EntityRenderer<PhysicsEntity> {
    public PhysicsBoxRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(PhysicsEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        // 1. Interpolate Center Position (EntityRenderer is already at 'entity.position')
        // We only need the difference between feet and center of mass
        float halfHeight = entity.getBbHeight() * 0.5f;
        poseStack.translate(0, halfHeight, 0);

        // 2. Slerp/Lerp the Rotation
        Matrix3f interpolatedRot = new Matrix3f(entity.prevRotation);
        interpolatedRot.lerp(entity.hitbox.rotation, partialTicks);
        poseStack.mulPoseMatrix(new Matrix4f(interpolatedRot));

        // 3. High Performance Lines
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.lines());
        renderLocalOutline(entity.hitbox, poseStack, vertexConsumer);

        poseStack.popPose();
        // Do NOT call super.render if you don't want the vanilla shadow/nametag to glitch
    }

    private void renderLocalOutline(OrientedBoundingBox obb, PoseStack ps, VertexConsumer buffer) {
        Matrix4f matrix = ps.last().pose();

        float x = (float) obb.halfExtents.x;
        float y = (float) obb.halfExtents.y;
        float z = (float) obb.halfExtents.z;

        // Draw the 12 edges of the box
        // Bottom square
        line(matrix, buffer, -x, -y, -z, x, -y, -z);
        line(matrix, buffer, x, -y, -z, x, -y, z);
        line(matrix, buffer, x, -y, z, -x, -y, z);
        line(matrix, buffer, -x, -y, z, -x, -y, -z);

        // Top square
        line(matrix, buffer, -x, y, -z, x, y, -z);
        line(matrix, buffer, x, y, -z, x, y, z);
        line(matrix, buffer, x, y, z, -x, y, z);
        line(matrix, buffer, -x, y, z, -x, y, -z);

        // Pillars
        line(matrix, buffer, -x, -y, -z, -x, y, -z);
        line(matrix, buffer, x, -y, -z, x, y, -z);
        line(matrix, buffer, x, -y, z, x, y, z);
        line(matrix, buffer, -x, -y, z, -x, y, z);
    }

    private void line(Matrix4f matrix, VertexConsumer buffer, float x1, float y1, float z1, float x2, float y2, float z2) {
        // Green color for physics debug
        buffer.vertex(matrix, x1, y1, z1).color(0, 255, 0, 255).normal(0, 1, 0).endVertex();
        buffer.vertex(matrix, x2, y2, z2).color(0, 255, 0, 255).normal(0, 1, 0).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(PhysicsEntity pEntity) {
        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/atlas.png");
    }
}