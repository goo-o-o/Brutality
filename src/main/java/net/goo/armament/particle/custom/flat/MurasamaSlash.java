package net.goo.armament.particle.custom.flat;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.goo.armament.particle.base.FlatParticleWithData;
import net.goo.armament.registry.ModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;

public class MurasamaSlash extends FlatParticleWithData {
    private final double size;

    protected MurasamaSlash(ClientLevel level, double x, double y, double z,
                            SpriteSet spriteSet, int EntityId, float size, float rotX, float rotY, float rotZ) {
        super(level, x, y, z, spriteSet, EntityId, size, rotX, rotY, rotZ);
        this.sprites = spriteSet;
        this.setSpriteFromAge(spriteSet);
        this.lifetime = 8;
        this.size = size;
    }

    @Override
    public void tick() {
        super.tick();
        Entity entity = getFromEntity();
        if (entity != null) {
            this.setPos(entity.getX(), entity.getY() + entity.getBbHeight() / 3, entity.getZ());
        }
    }

    public record ParticleData(int entityId, float size, float rotX, float rotY, float rotZ) implements ParticleOptions {
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
            return String.format(Locale.ROOT, "%s %d %.2f",
                    BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
                    entityId, size);
        }

        @Override
        public ParticleType<ParticleData> getType() {
            return ModParticles.MURASAMA_SLASH_PARTICLE.get();
        }

        public static Codec<ParticleData> CODEC(ParticleType<ParticleData> type) {
            return RecordCodecBuilder.create(instance ->
                    instance.group(
                            Codec.INT.fieldOf("entityId").forGetter(ParticleData::entityId),
                            Codec.FLOAT.fieldOf("size").forGetter(ParticleData::size),
                            Codec.FLOAT.fieldOf("rotX").forGetter(ParticleData::rotX),
                            Codec.FLOAT.fieldOf("rotY").forGetter(ParticleData::rotY),
                            Codec.FLOAT.fieldOf("rotY").forGetter(ParticleData::rotZ)
                    ).apply(instance, ParticleData::new)
            );
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<ParticleData> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(ParticleData data, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new MurasamaSlash(level, x, y, z,
                    this.spriteSet,
                    data.entityId(),
                    data.size(),
                    data.rotX(),
                    data.rotY(),
                    data.rotZ()
            );

        }
    }
}
