package net.goo.brutality.particle.custom;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.goo.brutality.Brutality;
import net.goo.brutality.particle.base.AbstractCameraAlignedTrailParticle;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class CelestialStarboardParticle extends AbstractCameraAlignedTrailParticle {


    public CelestialStarboardParticle(ClientLevel world, double x, double y, double z, float r, float g, float b, float width, int entityId, int sampleCount) {
        super(world, x, y, z, r, g, b, width, entityId, sampleCount);
        this.lifetime = 2000;
    }

    @Override
    protected ResourceLocation getTrailTexture() {
        return new ResourceLocation(Brutality.MOD_ID, "textures/particle/rainbow_trail_particle.png");
    }

    public void tick() {
        super.tick();
        float fade = 1F - age / (float) lifetime;
        this.trailA = fade * 2F;
        Entity from = this.getFromEntity();

        if (from == null) {
            remove();
        } else {
            this.y = from.getY();
            if (from.onGround()) this.remove();
        }

    }

    public static final class OrbFactory implements ParticleProvider<CelestialStarboardParticle.OrbData> {

        @Override
        public Particle createParticle(CelestialStarboardParticle.OrbData typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            CelestialStarboardParticle particle;
            particle = new CelestialStarboardParticle(worldIn, x, y, z, typeIn.r(), typeIn.g(), typeIn.b(), typeIn.width(),
                    typeIn.entityID(), typeIn.sampleCount());

            return particle;
        }


    }


    public record OrbData(float r, float g, float b, float width, int entityID, int sampleCount)
            implements ParticleOptions {
        public static final Deserializer<CelestialStarboardParticle.OrbData> DESERIALIZER = new Deserializer<>() {
            public CelestialStarboardParticle.@NotNull OrbData fromCommand(@NotNull ParticleType<CelestialStarboardParticle.OrbData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
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
                int sampleCount = reader.readInt();
                return new CelestialStarboardParticle.OrbData(r, g, b, width, EntityId, sampleCount);
            }

            public CelestialStarboardParticle.OrbData fromNetwork(ParticleType<CelestialStarboardParticle.OrbData> particleTypeIn, FriendlyByteBuf buffer) {
                return new CelestialStarboardParticle.OrbData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(),
                        buffer.readFloat(), buffer.readInt(), buffer.readInt());
            }
        };

        @Override
        public void writeToNetwork(FriendlyByteBuf buffer) {
            buffer.writeFloat(this.r);
            buffer.writeFloat(this.g);
            buffer.writeFloat(this.b);
            buffer.writeFloat(this.width);
            buffer.writeInt(this.entityID);
            buffer.writeInt(this.sampleCount);

        }

        @Override
        public String writeToString() {
            return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %d  %d",
                    BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
                    this.r, this.g, this.b,
                    this.width,
                    this.entityID,
                    this.sampleCount
            );
        }

        @Override
        public ParticleType<CelestialStarboardParticle.OrbData> getType() {
            return BrutalityModParticles.CELESTIAL_STARBOARD_PARTICLE.get();
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


        public static Codec<CelestialStarboardParticle.OrbData> CODEC(ParticleType<CelestialStarboardParticle.OrbData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                            Codec.FLOAT.fieldOf("r").forGetter(CelestialStarboardParticle.OrbData::r),
                            Codec.FLOAT.fieldOf("g").forGetter(CelestialStarboardParticle.OrbData::g),
                            Codec.FLOAT.fieldOf("b").forGetter(CelestialStarboardParticle.OrbData::b),
                            Codec.FLOAT.fieldOf("width").forGetter(CelestialStarboardParticle.OrbData::width),
                            Codec.INT.fieldOf("entityID").forGetter(CelestialStarboardParticle.OrbData::entityID),
                            Codec.INT.fieldOf("sampleCount").forGetter(CelestialStarboardParticle.OrbData::sampleCount)
                    ).apply(codecBuilder, CelestialStarboardParticle.OrbData::new)
            );
        }
    }

}