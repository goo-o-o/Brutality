package net.goo.brutality.particle.base;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.brutality.particle.providers.WaveParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import static net.goo.brutality.util.ModUtils.ModEasings.easeOut;

public class WaveParticle extends net.minecraft.client.particle.TextureSheetParticle {
    private static final Logger LOGGER = LogManager.getLogger();
    private final float radius;
    private final SpriteSet sprites;
    private static final org.joml.Quaternionf QUATERNION = new org.joml.Quaternionf(0F, -0.7F, 0.7F, 0F);

    public WaveParticle(ClientLevel level, double x, double y, double z, WaveParticleData data, SpriteSet sprites) {
        super(level, x, y, z);
        this.radius = data.radius();
        int growthDuration = data.growthDuration();
        this.sprites = sprites;
        this.lifetime = growthDuration;
    }

    @Override
    public boolean shouldCull() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        // Update sprite for animation
        this.setSpriteFromAge(sprites);
    }


    @Override
    public void render(@NotNull VertexConsumer buffer, net.minecraft.client.Camera camera, float partialTicks) {
        float growthProgress = ((float) this.age + partialTicks) / this.lifetime;
        growthProgress = Mth.clamp(growthProgress, 0.0F, 1.0F);
        this.quadSize = radius * easeOut(growthProgress);
        this.alpha = 1f - easeOut(growthProgress);

        Vec3 cameraPos = camera.getPosition();
        float x = (float)(Mth.lerp(partialTicks, this.xo, this.x) - cameraPos.x());
        float y = (float)(Mth.lerp(partialTicks, this.yo, this.y) - cameraPos.y());
        float z = (float)(Mth.lerp(partialTicks, this.zo, this.z) - cameraPos.z());

        float size = this.getQuadSize(partialTicks);
        org.joml.Vector3f[] vertices = new org.joml.Vector3f[]{
                new org.joml.Vector3f(-1.0F, -1.0F, 0.0F),
                new org.joml.Vector3f(-1.0F, 1.0F, 0.0F),
                new org.joml.Vector3f(1.0F, 1.0F, 0.0F),
                new org.joml.Vector3f(1.0F, -1.0F, 0.0F)
        };

        for (org.joml.Vector3f vertex : vertices) {
            vertex.rotate(QUATERNION);
            vertex.mul(size);
            vertex.add(x, y, z);
        }

        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int light = this.getLightColor(partialTicks);

        // Render top face
        buffer.vertex(vertices[0].x(), vertices[0].y(), vertices[0].z()).uv(u1, v1).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
        buffer.vertex(vertices[1].x(), vertices[1].y(), vertices[1].z()).uv(u1, v0).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
        buffer.vertex(vertices[2].x(), vertices[2].y(), vertices[2].z()).uv(u0, v0).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
        buffer.vertex(vertices[3].x(), vertices[3].y(), vertices[3].z()).uv(u0, v1).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();

        // Render bottom face
        buffer.vertex(vertices[3].x(), vertices[3].y(), vertices[3].z()).uv(u0, v1).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
        buffer.vertex(vertices[2].x(), vertices[2].y(), vertices[2].z()).uv(u0, v0).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
        buffer.vertex(vertices[1].x(), vertices[1].y(), vertices[1].z()).uv(u1, v0).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
        buffer.vertex(vertices[0].x(), vertices[0].y(), vertices[0].z()).uv(u1, v1).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}