package net.goo.brutality.particle.base;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.goo.brutality.registry.ModParticles;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Locale;

public class FlatParticleWithData extends FlatParticle {
    private final float size, rotX, rotY, rotZ;
    protected final int entityId;

    protected FlatParticleWithData(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, int entityId, float size, float rotX, float rotY, float rotZ) {
        super(level, x, y, z, spriteSet);
        this.entityId = entityId;
        this.sprites = spriteSet;
        this.setSpriteFromAge(spriteSet);
        this.lifetime = 20;
        this.size = size;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
    }


    public Entity getFromEntity() {
        return entityId == -1 ? null : level.getEntity(entityId);
    }

    @Override
    public float getQuadSize(float pScaleFactor) {
        return this.size;
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        Vec3 cameraPos = camera.getPosition();
        float x = (float)(Mth.lerp(partialTicks, this.xo, this.x) - cameraPos.x());
        float y = (float)(Mth.lerp(partialTicks, this.yo, this.y) - cameraPos.y());
        float z = (float)(Mth.lerp(partialTicks, this.zo, this.z) - cameraPos.z());

        // Create individual rotation quaternions (from radians to deg)
        Quaternionf pitchRot = new Quaternionf().rotationX((float) (this.rotX * (Math.PI / 180))); // X-axis (pitch)
        Quaternionf yawRot = new Quaternionf().rotationY((float) (this.rotY * (Math.PI / 180)));   // Y-axis (yaw)
        Quaternionf rollRot = new Quaternionf().rotationZ((float) (this.rotZ * (Math.PI / 180)));  // Z-axis (roll)

        // Combine rotations (order matters: yaw -> pitch -> roll -> custom QUATERNION)
        Quaternionf combinedRot = new Quaternionf();
        combinedRot.mul(yawRot)
                .mul(pitchRot)
                .mul(rollRot)
                .mul(QUATERNION);

        // Base quad vertices (before transformations)
        Vector3f[] baseVertices = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, -1.0F, 0.0F)
        };

        float size = this.getQuadSize(partialTicks);
        Vector3f[] transformedVertices = new Vector3f[4];

        // Transform each vertex
        for (int i = 0; i < 4; i++) {
            transformedVertices[i] = new Vector3f(baseVertices[i]);
            transformedVertices[i].rotate(combinedRot);  // Apply combined rotation
            transformedVertices[i].mul(size);           // Apply size scaling
            transformedVertices[i].add(x, y, z);        // Apply position offset
        }

        // Get UV coordinates and light value
        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int light = this.getLightColor(partialTicks);

        // Render top face (counter-clockwise winding)
        buffer.vertex(transformedVertices[0].x(), transformedVertices[0].y(), transformedVertices[0].z())
                .uv(u1, v1).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
        buffer.vertex(transformedVertices[1].x(), transformedVertices[1].y(), transformedVertices[1].z())
                .uv(u1, v0).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
        buffer.vertex(transformedVertices[2].x(), transformedVertices[2].y(), transformedVertices[2].z())
                .uv(u0, v0).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
        buffer.vertex(transformedVertices[3].x(), transformedVertices[3].y(), transformedVertices[3].z())
                .uv(u0, v1).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();

        // Render bottom face (clockwise winding)
        buffer.vertex(transformedVertices[3].x(), transformedVertices[3].y(), transformedVertices[3].z())
                .uv(u0, v1).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
        buffer.vertex(transformedVertices[2].x(), transformedVertices[2].y(), transformedVertices[2].z())
                .uv(u0, v0).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
        buffer.vertex(transformedVertices[1].x(), transformedVertices[1].y(), transformedVertices[1].z())
                .uv(u1, v0).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
        buffer.vertex(transformedVertices[0].x(), transformedVertices[0].y(), transformedVertices[0].z())
                .uv(u1, v1).color(rCol, gCol, bCol, alpha).uv2(light).endVertex();
    }

    public record ParticleData(int entityId, float size, float rotX, float rotY,
                               float rotZ) implements ParticleOptions {
        public static final Deserializer<ParticleData> DESERIALIZER = new Deserializer<>() {
            public ParticleData fromCommand(ParticleType<ParticleData> type, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                int entityId = reader.readInt();
                reader.expect(' ');
                float size = reader.readFloat();
                reader.expect(' ');
                float rotX = reader.readFloat();
                reader.expect(' ');
                float rotY = reader.readFloat();
                reader.expect(' ');
                float rotZ = reader.readFloat();
                return new ParticleData(entityId, size, rotX, rotY, rotZ);
            }

            public ParticleData fromNetwork(ParticleType<ParticleData> type, FriendlyByteBuf buffer) {
                return new ParticleData(buffer.readInt(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
            }
        };

        @Override
        public void writeToNetwork(FriendlyByteBuf buffer) {
            buffer.writeInt(entityId);
            buffer.writeFloat(size);
            buffer.writeFloat(rotX);
            buffer.writeFloat(rotY);
            buffer.writeFloat(rotZ);
        }

        @Override
        public String writeToString() {
            return String.format(Locale.ROOT, "%s %d %.2f", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), entityId, size, rotX, rotY, rotZ);
        }

        @Override
        public ParticleType<ParticleData> getType() {
            return ModParticles.FLAT_PARTICLE_WITH_DATA.get();
        }

        public static Codec<ParticleData> CODEC(ParticleType<ParticleData> type) {
            return RecordCodecBuilder.create(instance -> instance.group(Codec.INT.fieldOf("entityId").forGetter(ParticleData::entityId), Codec.FLOAT.fieldOf("size").forGetter(ParticleData::size), Codec.FLOAT.fieldOf("rotX").forGetter(ParticleData::rotX), Codec.FLOAT.fieldOf("rotY").forGetter(ParticleData::rotY), Codec.FLOAT.fieldOf("rotY").forGetter(ParticleData::rotZ)).apply(instance, ParticleData::new));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<ParticleData> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(ParticleData data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FlatParticleWithData(level, x, y, z, this.spriteSet, data.entityId(), data.size(), data.rotX(), data.rotY(), data.rotZ());

        }
    }
}
