package net.goo.brutality.client.particle.base;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.brutality.client.particle.providers.PointToPointParticleData;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import static net.minecraft.client.renderer.LightTexture.FULL_BRIGHT;

public class PointToPointParticle extends TextureSheetParticle {
    protected final SpriteSet sprites;
    protected final float x0, y0, z0, x1, y1, z1;
    protected final float height;

    public PointToPointParticle(ClientLevel level, double x, double y, double z, PointToPointParticleData<?> data, SpriteSet sprites) {
        super(level, x, y, z);
        this.setParticleSpeed(0, 0, 0);
        this.lifetime = 20;
        this.sprites = sprites;
        this.x0 = data.x0();
        this.y0 = data.y0();
        this.z0 = data.z0();
        this.x1 = data.x1();
        this.y1 = data.y1();
        this.z1 = data.z1();
        this.height = 1;
        this.setPos(x0, y0, z0); // Initialize position at first point
    }

    @Override
    public boolean shouldCull() {
        return false;
    }

    @Override
    public void render(@NotNull VertexConsumer buffer, Camera camera, float partialTicks) {
        Vec3 cameraPos = camera.getPosition();
        float px0 = (float)(this.x0 - cameraPos.x());
        float py0 = (float)(this.y0 - cameraPos.y());
        float pz0 = (float)(this.z0 - cameraPos.z());
        float px1 = (float)(this.x1 - cameraPos.x());
        float py1 = (float)(this.y1 - cameraPos.y());
        float pz1 = (float)(this.z1 - cameraPos.z());

        // Calculate direction and length
        Vector3f direction = new Vector3f(px1 - px0, 0, pz1 - pz0).normalize();
        float length = (float)Math.sqrt((px1 - px0) * (px1 - px0) + (pz1 - pz0) * (pz1 - pz0));
        Vector3f up = new Vector3f(0, 1, 0);
        Vector3f side = direction.cross(up).normalize().mul(0.001F);

        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int light = getLightColor(1);

        // Draw multiple planes along the length
        float segmentLength = 1.0F; // Each plane is 1 block long
        int segments = (int)Math.ceil(length / segmentLength);
        for (int i = 0; i < segments; i++) {
            float t0 = i * segmentLength;
            float t1 = Math.min((i + 1) * segmentLength, length);
            float frac0 = t0 / length;
            float frac1 = t1 / length;

            // Interpolate positions
            float sx0 = px0 + (px1 - px0) * frac0;
            float sy0 = py0 + (py1 - py0) * frac0;
            float sz0 = pz0 + (pz1 - pz0) * frac0;
            float sx1 = px0 + (px1 - px0) * frac1;
            float sy1 = py0 + (py1 - py0) * frac1;
            float sz1 = pz0 + (pz1 - pz0) * frac1;

            // Define vertices for a vertical plane segment
            Vector3f[] vertices = new Vector3f[]{
                    new Vector3f(sx0, sy0, sz0).add(side),               // Bottom left
                    new Vector3f(sx0, sy0 + height, sz0).add(side),      // Top left
                    new Vector3f(sx1, sy1, sz1).add(side),               // Bottom right
                    new Vector3f(sx1, sy1 + height, sz1).add(side)       // Top right
            };

            // Render front side
            buffer.vertex(vertices[0].x(), vertices[0].y(), vertices[0].z())
                    .uv(u0, v1).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
            buffer.vertex(vertices[1].x(), vertices[1].y(), vertices[1].z())
                    .uv(u0, v0).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
            buffer.vertex(vertices[3].x(), vertices[3].y(), vertices[3].z())
                    .uv(u1, v0).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
            buffer.vertex(vertices[2].x(), vertices[2].y(), vertices[2].z())
                    .uv(u1, v1).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();

            // Render back side
            buffer.vertex(vertices[2].x(), vertices[2].y(), vertices[2].z())
                    .uv(u1, v1).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
            buffer.vertex(vertices[3].x(), vertices[3].y(), vertices[3].z())
                    .uv(u1, v0).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
            buffer.vertex(vertices[1].x(), vertices[1].y(), vertices[1].z())
                    .uv(u0, v0).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
            buffer.vertex(vertices[0].x(), vertices[0].y(), vertices[0].z())
                    .uv(u0, v1).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
        }
    }

    @Override
    public void tick() {
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
        }
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return FULL_BRIGHT;
    }
}