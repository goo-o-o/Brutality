package net.goo.brutality.particle.custom.flat;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.goo.brutality.particle.base.FlatParticleWithData;
import net.goo.brutality.registry.BrutalityModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

import java.util.Locale;

public class MurasamaSlash extends FlatParticleWithData {
    private final boolean shouldFollow;
    protected float xOffset, yOffset, zOffset;

    protected MurasamaSlash(ClientLevel level, double x, double y, double z,
                            SpriteSet spriteSet, boolean shouldFollow, float xOffset, float yOffset, float zOffset, int entityId, float size, float rotX, float rotY, float rotZ) {
        super(level, x, y, z, spriteSet, entityId, size, rotX, rotY, rotZ);
        this.lifetime = 8;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
        this.shouldFollow = shouldFollow;
    }

    @Override
    public void tick() {
        super.tick();
        Entity entity = getFromEntity();
        if (entity != null && shouldFollow) {
            this.setPos(entity.getX() + xOffset, entity.getY() + yOffset, entity.getZ() + zOffset);
        }
    }

    public record ParticleData(boolean shouldFollow, float xOffset, float yOffset, float zOffset, int entityId, float size, float rotX, float rotY, float rotZ) implements ParticleOptions {
        public static final Deserializer<ParticleData> DESERIALIZER = new Deserializer<>() {
            public ParticleData fromCommand(ParticleType<ParticleData> type, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                boolean shouldFollow = reader.readBoolean();
                reader.expect(' ');
                float xOffset = reader.readFloat();
                reader.expect(' ');
                float yOffset = reader.readFloat();
                reader.expect(' ');
                float zOffset = reader.readFloat();
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
                return new ParticleData(shouldFollow, xOffset, yOffset, zOffset, entityId, size, rotX, rotY, rotZ);
            }

            public ParticleData fromNetwork(ParticleType<ParticleData> type, FriendlyByteBuf buffer) {
                return new ParticleData(
                        buffer.readBoolean(),
                        buffer.readFloat(), buffer.readFloat(), buffer.readFloat(),
                        buffer.readInt(),
                        buffer.readFloat(),
                        buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
            }
        };

        @Override
        public void writeToNetwork(FriendlyByteBuf buffer) {
            buffer.writeBoolean(shouldFollow);
            buffer.writeFloat(xOffset);
            buffer.writeFloat(yOffset);
            buffer.writeFloat(zOffset);
            buffer.writeInt(entityId);
            buffer.writeFloat(size);
            buffer.writeFloat(rotX);
            buffer.writeFloat(rotY);
            buffer.writeFloat(rotZ);
        }

        @Override
        public String writeToString() {
            return String.format(Locale.ROOT, "%b %s %d %.2f",
                    BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
                    entityId, size);
        }

        @Override
        public ParticleType<ParticleData> getType() {
            return BrutalityModParticles.MURASAMA_SLASH_PARTICLE.get();
        }

        public static Codec<ParticleData> CODEC(ParticleType<ParticleData> type) {
            return RecordCodecBuilder.create(instance ->
                    instance.group(
                            Codec.BOOL.fieldOf("shouldFollow").forGetter(ParticleData::shouldFollow),
                            Codec.FLOAT.fieldOf("xOffset").forGetter(ParticleData::xOffset),
                            Codec.FLOAT.fieldOf("yOffset").forGetter(ParticleData::yOffset),
                            Codec.FLOAT.fieldOf("zOffset").forGetter(ParticleData::zOffset),
                            Codec.INT.fieldOf("entityId").forGetter(ParticleData::entityId),
                            Codec.FLOAT.fieldOf("size").forGetter(ParticleData::size),
                            Codec.FLOAT.fieldOf("rotX").forGetter(ParticleData::rotX),
                            Codec.FLOAT.fieldOf("rotY").forGetter(ParticleData::rotY),
                            Codec.FLOAT.fieldOf("rotZ").forGetter(ParticleData::rotZ)
                    ).apply(instance, ParticleData::new)
            );
        }
    }

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
                    data.shouldFollow(),
                    data.xOffset(),
                    data.yOffset(),
                    data.zOffset(),
                    data.entityId(),
                    data.size(),
                    data.rotX(),
                    data.rotY(),
                    data.rotZ()
            );

        }
    }

}
