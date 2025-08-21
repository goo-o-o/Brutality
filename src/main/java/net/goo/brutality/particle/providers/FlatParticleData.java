package net.goo.brutality.particle.providers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.goo.brutality.particle.base.FlatParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class FlatParticleData<T extends ParticleOptions> implements ParticleOptions {
    public static final Codec<FlatParticleData<?>> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ForgeRegistries.PARTICLE_TYPES.getCodec().fieldOf("type").forGetter(data -> data.type),
                    Codec.FLOAT.fieldOf("radius").forGetter(data -> data.radius),
                    Codec.FLOAT.fieldOf("rotX").forGetter(data -> data.rotX),
                    Codec.FLOAT.fieldOf("rotY").forGetter(data -> data.rotY),
                    Codec.FLOAT.fieldOf("rotZ").forGetter(data -> data.rotZ),
                    Codec.FLOAT.fieldOf("xOffset").forGetter(data -> data.xOffset),
                    Codec.FLOAT.fieldOf("yOffset").forGetter(data -> data.yOffset),
                    Codec.FLOAT.fieldOf("zOffset").forGetter(data -> data.zOffset),
                    Codec.INT.optionalFieldOf("entityID").forGetter(data -> data.entityID == null ? java.util.Optional.empty() : java.util.Optional.of(data.entityID))
            ).apply(instance,
                    (type, radius, rotX, rotY, rotZ, xOffset, yOffset, zOffset, entityID) ->
                            new FlatParticleData<>((ParticleType<?>) type, radius, rotX, rotY, rotZ, xOffset, yOffset, zOffset, entityID.orElse(null)))
    );

    protected final ParticleType<T> type;
    protected final float radius;
    protected final float rotX;
    protected final float rotY;
    protected final float rotZ;
    protected final float xOffset;
    protected final float yOffset;
    protected final float zOffset;
    protected final Integer entityID;

    public FlatParticleData(ParticleType<T> type, float radius, float rotX, float rotY, float rotZ, float xOffset, float yOffset, float zOffset, Integer entityID) {
        this.type = type;
        this.radius = radius;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
        this.entityID = entityID;
    }

    public FlatParticleData(ParticleType<T> type, float radius, float rotX, float rotY, float rotZ) {
        this(type, radius, rotX, rotY, rotZ, 0, 0, 0, null);
    }

    public FlatParticleData(ParticleType<T> type, float radius) {
        this(type, radius, 0, 0, 0, 0, 0, 0, null);
    }

    public float radius() {
        return radius;
    }

    public float rotX() {
        return rotX;
    }

    public float rotY() {
        return rotY;
    }

    public float rotZ() {
        return rotZ;
    }

    public float xOffset() {
        return xOffset;
    }

    public float yOffset() {
        return yOffset;
    }

    public float zOffset() {
        return zOffset;
    }

    public Integer entityID() {
        return entityID;
    }

    @Override
    public @NotNull ParticleType<T> getType() {
        return type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeFloat(radius);
        buf.writeFloat(rotX);
        buf.writeFloat(rotY);
        buf.writeFloat(rotZ);
        buf.writeFloat(xOffset);
        buf.writeFloat(yOffset);
        buf.writeFloat(zOffset);
        buf.writeBoolean(entityID != null);
        if (entityID != null)
            buf.writeInt(entityID);
    }

    @Override
    public @NotNull String writeToString() {
        if (entityID != null)
            return String.format("%s %.2f %.2f %.2f %.2f %.2f %.2f %.2f %d",
                    ForgeRegistries.PARTICLE_TYPES.getKey(getType()), radius, rotX, rotY, rotZ, xOffset, yOffset, zOffset, entityID);
        return String.format("%s %.2f %.2f %.2f %.2f %.2f %.2f %.2f ",
                ForgeRegistries.PARTICLE_TYPES.getKey(getType()), radius, rotX, rotY, rotZ, xOffset, yOffset, zOffset);
    }

    public static final Deserializer<FlatParticleData<?>> DESERIALIZER = new Deserializer<>() {
        @Override
        public @NotNull FlatParticleData<?> fromCommand(@NotNull ParticleType<FlatParticleData<?>> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float radius = reader.readFloat();
            reader.expect(' ');
            float rotX = reader.readFloat();
            reader.expect(' ');
            float rotY = reader.readFloat();
            reader.expect(' ');
            float rotZ = reader.readFloat();
            reader.expect(' ');
            float xOffset = reader.readFloat();
            reader.expect(' ');
            float yOffset = reader.readFloat();
            reader.expect(' ');
            float zOffset = reader.readFloat();
            Integer entityID = null;
            if (reader.canRead() && reader.peek() == ' ') {
                reader.expect(' ');
                entityID = reader.readInt();
            }
            return new FlatParticleData<>(type, radius, rotX, rotY, rotZ, xOffset, yOffset, zOffset, entityID);
        }

        @Override
        public @NotNull FlatParticleData<?> fromNetwork(@NotNull ParticleType<FlatParticleData<?>> type, FriendlyByteBuf buf) {
            float radius = buf.readFloat();
            float rotX = buf.readFloat();
            float rotY = buf.readFloat();
            float rotZ = buf.readFloat();
            float xOffset = buf.readFloat();
            float yOffset = buf.readFloat();
            float zOffset = buf.readFloat();
            Integer entityID = null;
            if (buf.readBoolean()) {
                entityID = buf.readInt();
            }
            return new FlatParticleData<>(type, radius, rotX, rotY, rotZ, xOffset, yOffset, zOffset, entityID);
        }
    };

    public static class FlatParticleProvider implements ParticleProvider<FlatParticleData<?>> {
        private final SpriteSet sprites;

        public FlatParticleProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(@NotNull FlatParticleData data, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FlatParticle(level, x, y, z, data, sprites);
        }
    }
}