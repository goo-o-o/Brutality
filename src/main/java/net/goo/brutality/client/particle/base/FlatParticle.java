package net.goo.brutality.client.particle.base;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.brutality.client.particle.providers.FlatParticleData;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static net.minecraft.client.renderer.LightTexture.FULL_BRIGHT;


public class FlatParticle extends TextureSheetParticle {
    protected SpriteSet sprites;
    protected static final Quaternionf QUATERNION = new Quaternionf(0F, -0.7F, 0.7F, 0F);
    protected final float rotX, rotY, rotZ, xOffset, yOffset, zOffset;
    protected final Entity relatedEntity;

    public FlatParticle(ClientLevel level, double x, double y, double z, FlatParticleData<?> data, SpriteSet sprites) {
        super(level, x, y, z, 0.0, 0.0, 0.0);
        this.setParticleSpeed(0D, 0D, 0D);
        this.lifetime = 20;
        this.sprites = sprites;
        this.rotX = data.rotX();
        this.rotY = data.rotY();
        this.rotZ = data.rotZ();
        this.xOffset = data.xOffset();
        this.yOffset = data.yOffset();
        this.zOffset = data.zOffset();
        this.quadSize = data.radius();
        if (data.entityID() != null)
            this.relatedEntity = level.getEntity(data.entityID());
        else this.relatedEntity = null;
        this.setSpriteFromAge(sprites);
    }


    @Override
    public boolean shouldCull() {
        return false;
    }

    @Override
    public void render(@NotNull VertexConsumer buffer, Camera camera, float partialTicks) {
        Vec3 cameraPos = camera.getPosition();
        float x = (float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPos.x());
        float y = (float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPos.y());
        float z = (float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPos.z());

        Quaternionf pitchRot = new Quaternionf().rotationX((float) (this.rotX * (Math.PI / 180))); // X-axis (pitch)
        Quaternionf yawRot = new Quaternionf().rotationY((float) (this.rotY * (Math.PI / 180)));   // Y-axis (yaw)
        Quaternionf rollRot = new Quaternionf().rotationZ((float) (this.rotZ * (Math.PI / 180)));  // Z-axis (roll)

        Quaternionf combinedRot = new Quaternionf();
        combinedRot.mul(yawRot)
                .mul(pitchRot)
                .mul(rollRot)
                .mul(QUATERNION);

        Vector3f[] baseVertices = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, -1.0F, 0.0F)
        };

        float size = this.getQuadSize(partialTicks);
        Vector3f[] transformedVertices = new Vector3f[4];

        
        for (int i = 0; i < 4; i++) {
            transformedVertices[i] = new Vector3f(baseVertices[i]);
            transformedVertices[i].rotate(combinedRot);  // Apply combined rotation
            transformedVertices[i].mul(size);           // Apply size scaling
            transformedVertices[i].add(x, y, z);        // Apply position offset
        }

        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int light = this.getLightColor(partialTicks);

        buffer.vertex(transformedVertices[0].x(), transformedVertices[0].y(), transformedVertices[0].z())
                .uv(u1, v1).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
        buffer.vertex(transformedVertices[1].x(), transformedVertices[1].y(), transformedVertices[1].z())
                .uv(u1, v0).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
        buffer.vertex(transformedVertices[2].x(), transformedVertices[2].y(), transformedVertices[2].z())
                .uv(u0, v0).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
        buffer.vertex(transformedVertices[3].x(), transformedVertices[3].y(), transformedVertices[3].z())
                .uv(u0, v1).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();

        buffer.vertex(transformedVertices[3].x(), transformedVertices[3].y(), transformedVertices[3].z())
                .uv(u0, v1).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
        buffer.vertex(transformedVertices[2].x(), transformedVertices[2].y(), transformedVertices[2].z())
                .uv(u0, v0).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
        buffer.vertex(transformedVertices[1].x(), transformedVertices[1].y(), transformedVertices[1].z())
                .uv(u1, v0).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
        buffer.vertex(transformedVertices[0].x(), transformedVertices[0].y(), transformedVertices[0].z())
                .uv(u1, v1).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected int getLightColor(float pPartialTick) {
        return FULL_BRIGHT;
    }
}