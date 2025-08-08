package net.goo.brutality.particle.base;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.goo.brutality.registry.BrutalityModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class GenericTridentTrailParticle extends AbstractWorldAlignedTrailParticle {


    public GenericTridentTrailParticle(ClientLevel world, double x, double y, double z, float r, float g, float b, float width, int EntityId, float pitch, float yaw, float roll, String texture, int sampleCount) {
        super(world, x, y, z, r, g, b, width, EntityId, pitch, yaw, roll, texture, sampleCount);
    }

    @Override
    public Vec3 getEntityCenter() {
        Entity from = this.getFromEntity();
        if (from != null) {
            Vec3 movement = getRenderedDirection();

            return from.position().add(movement.scale(-1F));
        }
        return new Vec3(this.x, this.y - 0.25, this.z);
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


    public static final class OrbFactory implements ParticleProvider<GenericTridentTrailParticle.OrbData> {

        @Override
        public Particle createParticle(GenericTridentTrailParticle.OrbData typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            GenericTridentTrailParticle particle;
            particle = new GenericTridentTrailParticle(worldIn, x, y, z, typeIn.r(), typeIn.g(), typeIn.b(), typeIn.width(),
                    typeIn.entityID(), typeIn.pitch(), typeIn.yaw(), typeIn.roll(), typeIn.texture(), typeIn.sampleCount());

            return particle;
        }
    }


    public record OrbData(float r, float g, float b, float width, int entityID, float pitch, float yaw, float roll, String texture, int sampleCount)
            implements ParticleOptions {
        public static final Deserializer<GenericTridentTrailParticle.OrbData> DESERIALIZER = new Deserializer<>() {
            public GenericTridentTrailParticle.@NotNull OrbData fromCommand(@NotNull ParticleType<GenericTridentTrailParticle.OrbData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                float r = reader.readFloat();
                reader.expect(' ');
                float g = reader.readFloat();
                reader.expect(' ');
                float b = reader.readFloat();
                reader.expect(' ');
                float width = reader.readFloat();
                reader.expect(' ');
                int EntityId = reader.readInt();
                reader.expect(' ');
                float pitch = reader.readFloat();
                reader.expect(' ');
                float yaw = reader.readFloat();
                reader.expect(' ');
                float roll = reader.readFloat();
                reader.expect(' ');
                String texture = reader.readString();
                reader.expect(' ');
                int sampleCount = reader.readInt();
                return new GenericTridentTrailParticle.OrbData(r, g, b, width, EntityId, pitch, yaw, roll, texture, sampleCount);
            }

            public GenericTridentTrailParticle.OrbData fromNetwork(ParticleType<GenericTridentTrailParticle.OrbData> particleTypeIn, FriendlyByteBuf buffer) {
                return new GenericTridentTrailParticle.OrbData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(),
                        buffer.readFloat(), buffer.readInt(),
                        buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readUtf(), buffer.readInt());
            }
        };

        @Override
        public void writeToNetwork(FriendlyByteBuf buffer) {
            buffer.writeFloat(this.r);
            buffer.writeFloat(this.g);
            buffer.writeFloat(this.b);
            buffer.writeFloat(this.width);
            buffer.writeInt(this.entityID);
            buffer.writeFloat(this.pitch);
            buffer.writeFloat(this.yaw);
            buffer.writeFloat(this.roll);
            buffer.writeUtf(this.texture);
            buffer.writeInt(this.sampleCount);

        }

        @Override
        public String writeToString() {
            return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %d %.2f %.2f %.2f %s %d",
                    BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
                    this.r, this.g, this.b,
                    this.width,
                    this.entityID,
                    this.pitch, this.yaw, this.roll, this.sampleCount
            );
        }

        @Override
        public ParticleType<GenericTridentTrailParticle.OrbData> getType() {
            return BrutalityModParticles.GENERIC_TRIDENT_TRAIL_PARTICLE.get();
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
        public int entityID() {
            return this.entityID;
        }

        @Override
        public float pitch() {
            return this.pitch;
        }

        @Override
        public float yaw() {
            return this.yaw;
        }

        @Override
        public float roll() {
            return this.roll;
        }

        @Override
        public String texture() {
            return this.texture;
        }

        @Override
        public int sampleCount() {
            return this.sampleCount;
        }


        public Entity getOwner() {
            return this.getOwner();
        }

        public static Codec<GenericTridentTrailParticle.OrbData> CODEC(ParticleType<GenericTridentTrailParticle.OrbData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                            Codec.FLOAT.fieldOf("r").forGetter(GenericTridentTrailParticle.OrbData::r),
                            Codec.FLOAT.fieldOf("g").forGetter(GenericTridentTrailParticle.OrbData::g),
                            Codec.FLOAT.fieldOf("b").forGetter(GenericTridentTrailParticle.OrbData::b),
                            Codec.FLOAT.fieldOf("width").forGetter(GenericTridentTrailParticle.OrbData::width),
                            Codec.INT.fieldOf("entityID").forGetter(GenericTridentTrailParticle.OrbData::entityID),
                            Codec.FLOAT.fieldOf("pitch").forGetter(GenericTridentTrailParticle.OrbData::pitch),
                            Codec.FLOAT.fieldOf("yaw").forGetter(GenericTridentTrailParticle.OrbData::yaw),
                            Codec.FLOAT.fieldOf("roll").forGetter(GenericTridentTrailParticle.OrbData::roll),
                            Codec.STRING.fieldOf("texture").forGetter(GenericTridentTrailParticle.OrbData::texture),
                            Codec.INT.fieldOf("sampleCount").forGetter(GenericTridentTrailParticle.OrbData::sampleCount)
                    ).apply(codecBuilder, GenericTridentTrailParticle.OrbData::new)
            );
        }
    }

}