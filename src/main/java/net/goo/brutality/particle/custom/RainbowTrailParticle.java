package net.goo.brutality.particle.custom;

import net.goo.brutality.entity.base.BrutalityAbstractTrident;
import net.goo.brutality.particle.base.TrailParticle;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class RainbowTrailParticle extends TrailParticle {



    public RainbowTrailParticle(ClientLevel world, double x, double y, double z, float r, float g, float b, float a, float width, int entityId, int sampleCount, SpriteSet sprite) {
        super(world, x, y, z, r, g, b, a, width, entityId, sampleCount, sprite);
        this.lifetime = 200;
    }


    public Vec3 getRenderedDirection() {
        Entity owner = this.getFromEntity();
        if (owner != null) {
            Vec3 movement = owner.getDeltaMovement();
            if (movement.lengthSqr() < 0.0001) { // Small value to account for floating point errors
                return new Vec3(0, 0, 0); // Not moving
            }
            return movement.normalize();
        }
        return new Vec3(this.x, this.y, this.z);
    }

    @Override
    public Vec3 getEntityCenter() {
        Entity from = this.getFromEntity();
        if (from != null) {
            if (from instanceof BrutalityAbstractTrident) {
                Vec3 movement = getRenderedDirection();

                return from.position().add(movement.scale(-1F));
            }
            return from.position().add(0, from.getBbHeight() / 2, 0);

        }
        return new Vec3(this.x, this.y - 0.25, this.z);
    }


    private static final int COLOR_RES = 64;
    private static final float[][] CACHED_COLORS = new float[COLOR_RES][3];

    static {
        for (int i = 0; i < COLOR_RES; i++) {
            int[] rgb = BrutalityTooltipHelper.intToRgb(Mth.hsvToRgb(i / (float) COLOR_RES, 1.0f, 0.75f));
            CACHED_COLORS[i][0] = rgb[0] / 255F;
            CACHED_COLORS[i][1] = rgb[1] / 255F;
            CACHED_COLORS[i][2] = rgb[2] / 255F;
        }
    }

//
//    @Override
//    public void render(@NotNull VertexConsumer consumer, @NotNull Camera camera, float partialTick) {
//        if (trailPointer > -1) {
//            MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
//            VertexConsumer vertexconsumer = getVertexConsumer(multibuffersource$buffersource);
//
//            Vec3 cameraPos = camera.getPosition();
//            double x = (float) (Mth.lerp(partialTick, this.xo, this.x));
//            double y = (float) (Mth.lerp(partialTick, this.yo, this.y));
//            double z = (float) (Mth.lerp(partialTick, this.zo, this.z));
//
//            PoseStack posestack = new PoseStack();
//            posestack.pushPose();
//            posestack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
//            int samples = 0;
//            Vec3 drawFrom = new Vec3(x, y, z);
//            float zRot = getTrailRot(camera);
//            Vec3 topAngleVec = new Vec3(0, getTrailWidth() / 2F, 0).zRot(zRot);
//            Vec3 bottomAngleVec = new Vec3(0, getTrailWidth() / -2F, 0).zRot(zRot);
//            PoseStack.Pose posestack$pose = posestack.last();
//            Matrix4f matrix4f = posestack$pose.pose();
//            Matrix3f matrix3f = posestack$pose.normal();
//            int totalSamples = sampleCount;
//            float invSampleCount = 1.0f / totalSamples;
//
//            int j = getLightColor(partialTick);
//            while (samples < sampleCount()) {
//                Vec3 sample = getTrailPosition(samples * sampleStep(), partialTick);
//                float u1 = samples * invSampleCount;
//                float u2 = u1 + invSampleCount;
//
//                Vec3 draw1 = drawFrom;
//
//                float hue = (u1 + (this.age % 1000) / 1000.0f) % 1.0f;
//                int idx = (int)(hue * COLOR_RES) % COLOR_RES;
//                float[] color = CACHED_COLORS[idx];
//                float r = color[0], g = color[1], b = color[2];
//
//
//
//                vertexconsumer.vertex(matrix4f, (float) draw1.x + (float) bottomAngleVec.x, (float) draw1.y + (float) bottomAngleVec.y,
//                                (float) draw1.z + (float) bottomAngleVec.z)
//                        .color(r, g, b, trailA).uv(u1, 1F).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
//                vertexconsumer.vertex(matrix4f, (float) sample.x + (float) bottomAngleVec.x, (float) sample.y + (float) bottomAngleVec.y,
//                                (float) sample.z + (float) bottomAngleVec.z)
//                        .color(r, g, b, trailA).uv(u2, 1F).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
//                vertexconsumer.vertex(matrix4f, (float) sample.x + (float) topAngleVec.x, (float) sample.y + (float) topAngleVec.y,
//                                (float) sample.z + (float) topAngleVec.z)
//                        .color(r, g, b, trailA).uv(u2, 0).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
//                vertexconsumer.vertex(matrix4f, (float) draw1.x + (float) topAngleVec.x, (float) draw1.y + (float) topAngleVec.y,
//                                (float) draw1.z + (float) topAngleVec.z)
//                        .color(r, g, b, trailA).uv(u1, 0).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
//                samples++;
//                drawFrom = sample;
//            }
//            multibuffersource$buffersource.endBatch();
//            posestack.popPose();
//        }
//    }


}