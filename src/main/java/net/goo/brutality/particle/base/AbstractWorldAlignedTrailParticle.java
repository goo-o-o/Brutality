package net.goo.brutality.particle.base;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

public class AbstractWorldAlignedTrailParticle extends TrailParticle {


    private final int entityId, sampleCount;
    private float width, pitch = 0, yaw = 0, roll = 0;
    private String texture = "";

    public AbstractWorldAlignedTrailParticle(ClientLevel world, double x, double y, double z, float r, float g, float b, float a, float width, int entityId, int sampleCount, SpriteSet sprite) {
        super(world, x, y, z, r, g, b, a, width, entityId, sampleCount, sprite);
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
        this.entityId = entityId;
        this.gravity = 0;
        this.lifetime = 2000;
        this.width = width;
        Vec3 vec3 = getEntityCenter();
        this.x = this.xo = vec3.x;
        this.y = this.yo = vec3.y;
        this.z = this.zo = vec3.z;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.setAlpha(0.25F);
        this.texture = texture;
        this.sampleCount = sampleCount;
    }

    public Vec3 getPlaneNormal() {
        Vector3f normal = new Vector3f(0, 1, 0); // Upward-facing normal

        Matrix4f rotation = new Matrix4f()
                .rotateY(yaw)   // horizontal turn
                .rotateZ(pitch) // vertical tilt
                .rotateX(roll); // roll

// Transform the normal vector using the rotation matrix
        Vector4f rotated = new Vector4f(normal, 0.0f).mul(rotation);

// Return as Vec3
        return new Vec3(rotated.x(), rotated.y(), rotated.z());

    }



//
//    @Override
//    public void render(@NotNull VertexConsumer consumer, @NotNull Camera camera, float partialTick) {
//        if (trailPointer > -1) {
//            Vec3 normal = getPlaneNormal();
//
//            // Apply width scaling in world space
//            Vec3 topOffset = normal.scale(width * 1.5);
//            Vec3 bottomOffset = normal.scale(-width * 1.5);
//
//            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
//            VertexConsumer vertexConsumer = getVertexConsumer(bufferSource);
//            Vec3 cameraPos = camera.getPosition();
//
//            double x = Mth.lerp(partialTick, this.xo, this.x);
//            double y = Mth.lerp(partialTick, this.yo, this.y);
//            double z = Mth.lerp(partialTick, this.zo, this.z);
//
//            PoseStack poseStack = new PoseStack();
//            poseStack.pushPose();
//            poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
//
//            int samples = 0;
//            Vec3 drawFrom = new Vec3(x, y, z);
//            int j = getLightColor(partialTick);
//
//            while (samples < sampleCount()) {
//                Vec3 sample = getTrailPosition(samples * sampleStep(), partialTick);
//                float u1 = samples / (float) sampleCount();
//                float u2 = u1 + 1 / (float) sampleCount();
//
//                PoseStack.Pose pose = poseStack.last();
//                Matrix4f matrix4f = pose.pose();
//                Matrix3f matrix3f = pose.normal();
//
//                vertexConsumer.vertex(matrix4f, (float) drawFrom.x + (float) bottomOffset.x,
//                                (float) drawFrom.y + (float) bottomOffset.y, (float) drawFrom.z + (float) bottomOffset.z)
//                        .color(trailR, trailG, trailB, trailA).uv(u1, 1F).overlayCoords(NO_OVERLAY).uv2(j)
//                        .normal(matrix3f, (float)normal.x, (float)normal.y, (float)normal.z).endVertex();
//
//                vertexConsumer.vertex(matrix4f, (float) sample.x + (float) bottomOffset.x,
//                                (float) sample.y + (float) bottomOffset.y, (float) sample.z + (float) bottomOffset.z)
//                        .color(trailR, trailG, trailB, trailA).uv(u2, 1F).overlayCoords(NO_OVERLAY).uv2(j)
//                        .normal(matrix3f, (float)normal.x, (float)normal.y, (float)normal.z).endVertex();
//
//                vertexConsumer.vertex(matrix4f, (float) sample.x + (float) topOffset.x,
//                                (float) sample.y + (float) topOffset.y, (float) sample.z + (float) topOffset.z)
//                        .color(trailR, trailG, trailB, trailA).uv(u2, 0).overlayCoords(NO_OVERLAY).uv2(j)
//                        .normal(matrix3f, (float)normal.x, (float)normal.y, (float)normal.z).endVertex();
//
//                vertexConsumer.vertex(matrix4f, (float) drawFrom.x + (float) topOffset.x,
//                                (float) drawFrom.y + (float) topOffset.y, (float) drawFrom.z + (float) topOffset.z)
//                        .color(trailR, trailG, trailB, trailA).uv(u1, 0).overlayCoords(NO_OVERLAY).uv2(j)
//                        .normal(matrix3f, (float)normal.x, (float)normal.y, (float)normal.z).endVertex();
//
//                samples++;
//                drawFrom = sample;
//            }
//
//            bufferSource.endBatch();
//            poseStack.popPose();
//        }
//    }


    public void tick() {
        super.tick();
        float fade = 1F - age / (float) lifetime;
        this.a = fade * 2F;

        Entity from = this.getFromEntity();
        if (from == null) {
            remove();
        }

    }
}