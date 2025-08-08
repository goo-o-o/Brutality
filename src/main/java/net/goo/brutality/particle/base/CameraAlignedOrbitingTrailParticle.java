package net.goo.brutality.particle.base;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.goo.brutality.Brutality;
import net.goo.brutality.registry.BrutalityModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class CameraAlignedOrbitingTrailParticle extends AbstractCameraAlignedTrailParticle {
    private static final ResourceLocation TRAIL_TEXTURE = new ResourceLocation(Brutality.MOD_ID, "textures/particle/circle_trail_particle.png");

    private final int entityId;
    private final float width;
    private final float height;
    private final float initialYRot;
    private final float rotateByAge;

    public CameraAlignedOrbitingTrailParticle(ClientLevel world, double x, double y, double z, float r, float g, float b, float width, int entityId, int sampleCount) {
        super(world, x, y, z, r, g, b, width, entityId, sampleCount);
        this.entityId = entityId;
        this.gravity = 0;
        this.lifetime = 20 + this.random.nextInt(20);
        initialYRot = random.nextFloat() * 360F;
        rotateByAge = (10 + random.nextFloat() * 10F) * (random.nextBoolean() ? -1F : 1F);
        this.width = width;
        this.height = width;
        Vec3 vec3 = getOrbitPosition();
        this.x = this.xo = vec3.x;
        this.y = this.yo = vec3.y;
        this.z = this.zo = vec3.z;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
    }


    public Vec3 getEntityPosition(){
        Entity from = this.getFromEntity();
        if(from != null){
            return from.position();
        }
        return new Vec3(this.x, this.y, this.z);
    }


    public Entity getFromEntity() {
        return entityId == -1 ? null : level.getEntity(entityId);
    }

    public Vec3 getOrbitPosition(){
        Vec3 dinoPos = getEntityPosition();
        Vec3 vec3 = new Vec3(0, height, width).yRot((float)Math.toRadians(initialYRot + rotateByAge * age));
        return dinoPos.add(vec3);
    }

    public void tick() {
        super.tick();
        this.trailA = 1F - age / (float) lifetime;
        Vec3 vec3 = getOrbitPosition();
        this.x = vec3.x;
        this.y = vec3.y;
        this.z = vec3.z;

        Entity from = this.getFromEntity();
        if(from == null){
            remove();
        }

    }


    @Override
    public ResourceLocation getTrailTexture() {
        return TRAIL_TEXTURE;
    }

    public static final class OrbFactory implements ParticleProvider<CameraAlignedOrbitingTrailParticle.OrbData> {

        @Override
        public Particle createParticle(CameraAlignedOrbitingTrailParticle.OrbData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            CameraAlignedOrbitingTrailParticle particle;
            particle = new CameraAlignedOrbitingTrailParticle(worldIn, x, y, z, typeIn.r(), typeIn.g(), typeIn.b(), typeIn.width(), typeIn.entityId(), typeIn.sampleCount());

            return particle;
        }
    }

    public record OrbData(float r, float g, float b, float width, int entityId,
                          int sampleCount) implements ParticleOptions {
            public static final Deserializer<OrbData> DESERIALIZER = new Deserializer<>() {
                public CameraAlignedOrbitingTrailParticle.@NotNull OrbData fromCommand(ParticleType<OrbData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
                    reader.expect(' ');
                    float r = reader.readFloat();
                    reader.expect(' ');
                    float g = reader.readFloat();
                    reader.expect(' ');
                    float b = reader.readFloat();
                    reader.expect(' ');
                    float width = reader.readFloat();
                    reader.expect(' ');
                    int entityId = reader.readInt();
                    reader.expect(' ');
                    int sampleCount = reader.readInt();
                    return new CameraAlignedOrbitingTrailParticle.OrbData(r, g, b, width, entityId, sampleCount);
                }

                public CameraAlignedOrbitingTrailParticle.OrbData fromNetwork(ParticleType<OrbData> particleTypeIn, FriendlyByteBuf buffer) {
                    return new CameraAlignedOrbitingTrailParticle.OrbData(
                            buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readInt(), buffer.readInt());
                }
            };

        @Override
            public void writeToNetwork(FriendlyByteBuf buffer) {
                buffer.writeFloat(this.r);
                buffer.writeFloat(this.g);
                buffer.writeFloat(this.b);
                buffer.writeFloat(this.width);
                buffer.writeInt(this.entityId);
                buffer.writeInt(this.sampleCount);
            }

            @Override
            public String writeToString() {
                return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %d %d", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
                        this.r, this.g, this.b, this.width, this.entityId, this.sampleCount);
            }

            @Override
            public ParticleType<OrbData> getType() {
                return BrutalityModParticles.CAMERA_ALIGNED_ORBITING_TRAIL_PARTICLE.get();
            }

        @Override
        public float r() {
                return this.r;
            }

        @Override
        public float g() {
                return this.g;
            }

        @Override
        public float b() {
                return this.b;
            }

        @Override
        public float width() {
                return this.width;
            }


        @Override
        public int entityId() {
                return this.entityId;
            }

        @Override
        public int sampleCount() {
                return this.sampleCount;
            }

            public static Codec<OrbData> CODEC(ParticleType<OrbData> particleType) {
                return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                                Codec.FLOAT.fieldOf("r").forGetter(CameraAlignedOrbitingTrailParticle.OrbData::r),
                                Codec.FLOAT.fieldOf("g").forGetter(CameraAlignedOrbitingTrailParticle.OrbData::g),
                                Codec.FLOAT.fieldOf("b").forGetter(CameraAlignedOrbitingTrailParticle.OrbData::b),
                                Codec.FLOAT.fieldOf("width").forGetter(CameraAlignedOrbitingTrailParticle.OrbData::width),
                                Codec.INT.fieldOf("entityId").forGetter(CameraAlignedOrbitingTrailParticle.OrbData::entityId),
                                Codec.INT.fieldOf("sampleCount").forGetter(CameraAlignedOrbitingTrailParticle.OrbData::sampleCount)
                        ).apply(codecBuilder, CameraAlignedOrbitingTrailParticle.OrbData::new)
                );
            }
        }

}