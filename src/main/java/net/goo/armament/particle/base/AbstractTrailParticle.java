package net.goo.armament.particle.base;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.armament.client.ArmaRenderTypes;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.Arrays;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

public abstract class AbstractTrailParticle extends Particle {

    private final Vec3[] trailPositions = new Vec3[64];
    public int trailPointer = -1;

    public float r;
    public float g;
    public float b;

    protected float trailA = 0.4F;

    public AbstractTrailParticle(ClientLevel world, double x, double y, double z, double xd, double yd, double zd, float r, float g, float b) {
        super(world, x, y, z);
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public void tick() {
        tickTrail();
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.xd *= 0.99;
        this.yd *= 0.99;
        this.zd *= 0.99;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.move(this.xd, this.yd, this.zd);
            this.yd -= (double) this.gravity;
        }
    }

    public void tickTrail() {

        Vec3 currentPosition = new Vec3(this.x, this.y, this.z);
        if (trailPointer == -1) {
            Arrays.fill(trailPositions, currentPosition);
        }
        if (++this.trailPointer == this.trailPositions.length) {
            this.trailPointer = 0;
        }
        this.trailPositions[this.trailPointer] = currentPosition;
    }


    // In AbstractTrailParticle.java
    @Override
    public void render(@NotNull VertexConsumer consumer, @NotNull Camera camera, float partialTick) {
        if (trailPointer > -1) {
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer vertexConsumer = getVertexConsumer(bufferSource);
            Vec3 cameraPos = camera.getPosition();

            double x = Mth.lerp(partialTick, this.xo, this.x);
            double y = Mth.lerp(partialTick, this.yo, this.y);
            double z = Mth.lerp(partialTick, this.zo, this.z);

            PoseStack poseStack = new PoseStack();
            poseStack.pushPose();
            poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

            int samples = 0;
            Vec3 drawFrom = new Vec3(x, y, z);
            int j = getLightColor(partialTick);

            // Define a fixed horizontal plane (no rotation)
            Vec3 topOffset = new Vec3(0, getTrailHeight() / 2F, 0); // Flat plane (Y-axis up)
            Vec3 bottomOffset = new Vec3(0, -getTrailHeight() / 2F, 0);

            while (samples < sampleCount()) {
                Vec3 sample = getTrailPosition(samples * sampleStep(), partialTick);
                float u1 = samples / (float) sampleCount();
                float u2 = u1 + 1 / (float) sampleCount();

                PoseStack.Pose pose = poseStack.last();
                Matrix4f matrix4f = pose.pose();
                Matrix3f matrix3f = pose.normal();

                // Render as a flat horizontal plane
                vertexConsumer.vertex(matrix4f, (float) drawFrom.x + (float) bottomOffset.x,
                                (float) drawFrom.y + (float) bottomOffset.y, (float) drawFrom.z + (float) bottomOffset.z)
                        .color(r, g, b, trailA).uv(u1, 1F).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0, 1, 0).endVertex();

                vertexConsumer.vertex(matrix4f, (float) sample.x + (float) bottomOffset.x,
                                (float) sample.y + (float) bottomOffset.y, (float) sample.z + (float) bottomOffset.z)
                        .color(r, g, b, trailA).uv(u2, 1F).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0, 1, 0).endVertex();

                vertexConsumer.vertex(matrix4f, (float) sample.x + (float) topOffset.x,
                                (float) sample.y + (float) topOffset.y, (float) sample.z + (float) topOffset.z)
                        .color(r, g, b, trailA).uv(u2, 0).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0, 1, 0).endVertex();

                vertexConsumer.vertex(matrix4f, (float) drawFrom.x + (float) topOffset.x,
                                (float) drawFrom.y + (float) topOffset.y, (float) drawFrom.z + (float) topOffset.z)
                        .color(r, g, b, trailA).uv(u1, 0).overlayCoords(NO_OVERLAY).uv2(j).normal(matrix3f, 0, 1, 0).endVertex();

                samples++;
                drawFrom = sample;
            }

            bufferSource.endBatch();
            poseStack.popPose();
        }
    }

    protected VertexConsumer getVertexConsumer(MultiBufferSource.BufferSource multibuffersource$buffersource) {
        return multibuffersource$buffersource.getBuffer(ArmaRenderTypes.LIGHT_TRAIL_EFFECT.apply(getTrailTexture()));
    }

    public float getTrailRot(Camera camera) {
        return -0.017453292F * camera.getXRot();
    }

    public abstract float getTrailHeight();


    public abstract ResourceLocation getTrailTexture();

    public int sampleCount() {
        return 20;
    }

    public int sampleStep() {
        return 1;
    }


    public Vec3 getTrailPosition(int pointer, float partialTick) {
        if (this.removed) {
            partialTick = 1.0F;
        }
        int i = this.trailPointer - pointer & 63;
        int j = this.trailPointer - pointer - 1 & 63;
        Vec3 d0 = this.trailPositions[j];
        Vec3 d1 = this.trailPositions[i].subtract(d0);
        return d0.add(d1.scale(partialTick));
    }

    public int getLightColor(float f) {
        return 240;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }
}