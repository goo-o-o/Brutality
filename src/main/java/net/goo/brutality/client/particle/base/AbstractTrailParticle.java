package net.goo.brutality.client.particle.base;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.brutality.client.renderers.BrutalityRenderTypes;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.Arrays;

public abstract class AbstractTrailParticle extends Particle {
    protected Vec3[] trailPositions = new Vec3[64];
    protected int trailPointer = -1;
    protected int trailR = 255;
    protected int trailG = 255;
    protected int trailB = 255;
    protected int trailA = 255;

    public AbstractTrailParticle(ClientLevel world, double x, double y, double z, double xd, double yd, double zd) {
        super(world, x, y, z);
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
    }

    public void tick() {
        this.tickTrail();
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
            this.yd -= this.gravity;
        }

    }

    public void tickTrail() {
        Vec3 currentPosition = new Vec3(this.x, this.y, this.z);
        if (this.trailPointer == -1) {
            Arrays.fill(this.trailPositions, currentPosition);
        }

        if (++this.trailPointer == this.trailPositions.length) {
            this.trailPointer = 0;
        }

        this.trailPositions[this.trailPointer] = currentPosition;
    }


    protected VertexConsumer getVertexConsumer(MultiBufferSource.BufferSource bufferSource) {
        return bufferSource.getBuffer(BrutalityRenderTypes.TRAIL_RENDER_TYPE.apply(this.getTrailTexture()));
    }

    public abstract float getTrailHeight();

    public abstract ResourceLocation getTrailTexture();

    public Vec3 getInterpolatedPosition(float partialTick) {
        return new Vec3(
                Mth.lerp(partialTick, this.xo, this.x),
                Mth.lerp(partialTick, this.yo, this.y),
                Mth.lerp(partialTick, this.zo, this.z)
        );
    }

    public void render(VertexConsumer consumer, Camera camera, float partialTick) {
        if (this.trailPointer > -1) {
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer vertexconsumer = this.getVertexConsumer(bufferSource);

            Vec3 cameraPos = camera.getPosition();
            // The "head" of the trail is the current interpolated position
            Vec3 currentPos = this.getInterpolatedPosition(partialTick);

            PoseStack posestack = new PoseStack();
            posestack.pushPose();
            posestack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

            Vec3 drawFrom = currentPos;
            float halfHeight = this.getTrailHeight() / 2.0F;

            for (int samples = 0; samples < this.sampleCount(); ++samples) {
                Vec3 sample = this.getTrailPosition(samples * this.sampleStep(), partialTick);
                int light = this.getLightColor(partialTick);

                // 1. Get the direction of the segment
                Vec3 segmentDir = drawFrom.subtract(sample).normalize();
                // 2. Get vector from segment to camera
                Vec3 toCamera = drawFrom.subtract(cameraPos).normalize();
                // 3. Cross product creates the "Width" vector perpendicular to both
                Vec3 sideVec = segmentDir.cross(toCamera).normalize().scale(halfHeight);

                float u1 = (float) samples / (float) this.sampleCount();
                float u2 = (float) (samples + 1) / (float) this.sampleCount();

                Matrix4f matrix4f = posestack.last().pose();
                Matrix3f matrix3f = posestack.last().normal();



                int color = getTrailColor((float) samples / sampleCount());

                // Bottom vertices (using sideVec)
                vertexconsumer.vertex(matrix4f, (float)(drawFrom.x - sideVec.x), (float)(drawFrom.y - sideVec.y), (float)(drawFrom.z - sideVec.z))
                        .color(color).uv(u1, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                vertexconsumer.vertex(matrix4f, (float)(sample.x - sideVec.x), (float)(sample.y - sideVec.y), (float)(sample.z - sideVec.z))
                        .color(color).uv(u2, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();

                // Top vertices
                vertexconsumer.vertex(matrix4f, (float)(sample.x + sideVec.x), (float)(sample.y + sideVec.y), (float)(sample.z + sideVec.z))
                        .color(color).uv(u2, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                vertexconsumer.vertex(matrix4f, (float)(drawFrom.x + sideVec.x), (float)(drawFrom.y + sideVec.y), (float)(drawFrom.z + sideVec.z))
                        .color(color).uv(u1, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();

                drawFrom = sample;
            }

//            bufferSource.endBatch();
            posestack.popPose();
        }
    }

    public int getTrailColor(float percentageFromHead) {
        return FastColor.ARGB32.color(trailA, trailR, trailB, trailG);
    }


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


    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }
}
