package net.goo.brutality.particle.base;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.brutality.client.BrutalityRenderTypes;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.Arrays;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

public class TrailParticle extends Particle {
    private final SpriteSet spriteSet;
    protected final Vec3[] trailPositions = new Vec3[64];
    protected final int entityId;
    protected final float r;
    protected final float g;
    protected final float b;
    protected float a;
    protected final float width;
    public final int sampleCount;
    protected int trailPointer = -1;

    public TrailParticle(ClientLevel level, double x, double y, double z, float r, float g, float b, float a, float width, int entityId, int sampleCount, SpriteSet spriteSet) {
        super(level, x, y, z);
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.width = width;
        this.entityId = entityId;
        this.sampleCount = sampleCount;
        this.spriteSet = spriteSet;
        this.lifetime = 200; // Adjust as needed
    }

    public Entity getFromEntity() {
        return entityId == -1 ? null : level.getEntity(entityId);
    }

    public Vec3 getEntityCenter() {
        Entity entity = getFromEntity();
        if (entity != null) {
            return entity.position().add(0, entity.getBbHeight() / 2, 0);
        }
        return new Vec3(this.x, this.y, this.z);
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 pos = getEntityCenter();
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;

        if (getFromEntity() == null) {
            remove();
        } else {
            tickTrail();
        }
    }

    protected void tickTrail() {
        Vec3 currentPosition = new Vec3(this.x, this.y, this.z);
        if (trailPointer == -1) {
            Arrays.fill(trailPositions, currentPosition);
        }
        trailPointer = (trailPointer + 1) % trailPositions.length;
        trailPositions[trailPointer] = currentPosition;
    }

    Vec3 getTrailPosition(int pointer, float partialTick) {
        if (this.removed) {
            partialTick = 1.0F;
        }
        int i = (trailPointer - pointer) & 63;
        int j = (trailPointer - pointer - 1) & 63;
        Vec3 pos0 = trailPositions[j];
        Vec3 pos1 = trailPositions[i];
        return pos0.add(pos1.subtract(pos0).scale(partialTick));
    }

    @Override
    public void render(@NotNull VertexConsumer consumer, @NotNull Camera camera, float partialTick) {
        if (trailPointer < 0) return;

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(BrutalityRenderTypes.LIGHT_TRAIL_EFFECT.apply(spriteSet.get(this.level.random).atlasLocation()));

        TextureAtlasSprite sprite = spriteSet.get(this.level.random); // Or specific texture
        Vec3 cameraPos = camera.getPosition();
        double x = Mth.lerp(partialTick, this.xo, this.x);
        double y = Mth.lerp(partialTick, this.yo, this.y);
        double z = Mth.lerp(partialTick, this.zo, this.z);

        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        // Camera-facing billboard setup
        Vec3 look = camera.getPosition().subtract(x, y, z).normalize();
        Vec3 right = new Vec3(0, 1, 0).cross(look).normalize().scale(width / 2.0F);
        Vec3 up = look.cross(right).normalize().scale(width / 2.0F);

        Matrix4f matrix4f = poseStack.last().pose();
        Matrix3f matrix3f = poseStack.last().normal(); // For normal vector
        int light = 15728880; // Full brightness

        // Normalize UVs to 0-1
        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();
        float uRange = u1 - u0;
        float vRange = v1 - v0;

        Vec3 drawFrom = new Vec3(x, y, z);
        for (int i = 0; i < sampleCount; i++) {
            Vec3 drawTo = getTrailPosition(i, partialTick);
            float u1Norm = i / (float) sampleCount;
            float u2Norm = (i + 1) / (float) sampleCount;
            float u1Scaled = u0 + uRange * u1Norm;
            float u2Scaled = u0 + uRange * u2Norm;

            // Define quad vertices (camera-aligned)
            Vec3 vert1 = drawFrom.add(right).add(up); // Top-right
            Vec3 vert2 = drawFrom.subtract(right).add(up); // Top-left
            Vec3 vert3 = drawTo.subtract(right).subtract(up); // Bottom-left
            Vec3 vert4 = drawTo.add(right).subtract(up); // Bottom-right

            vertexConsumer.vertex(matrix4f, (float) vert1.x, (float) vert1.y, (float) vert1.z)
                    .color(r, g, b, a).uv(u1Scaled, v0).overlayCoords(NO_OVERLAY).uv2(light)
                    .normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexConsumer.vertex(matrix4f, (float) vert2.x, (float) vert2.y, (float) vert2.z)
                    .color(r, g, b, a).uv(u2Scaled, v0).overlayCoords(NO_OVERLAY).uv2(light)
                    .normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexConsumer.vertex(matrix4f, (float) vert3.x, (float) vert3.y, (float) vert3.z)
                    .color(r, g, b, a).uv(u2Scaled, v1).overlayCoords(NO_OVERLAY).uv2(light)
                    .normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexConsumer.vertex(matrix4f, (float) vert4.x, (float) vert4.y, (float) vert4.z)
                    .color(r, g, b, a).uv(u1Scaled, v1).overlayCoords(NO_OVERLAY).uv2(light)
                    .normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();

            drawFrom = drawTo;
        }

        bufferSource.endBatch();
        poseStack.popPose();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @Override
    public boolean shouldCull() {
        return false;
    }
}