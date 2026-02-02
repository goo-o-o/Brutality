package net.goo.brutality.client.particle.providers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.goo.brutality.client.particle.base.PointToPointParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class PointToPointParticleData<T extends ParticleOptions> implements ParticleOptions {
    public static final Codec<PointToPointParticleData<?>> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ForgeRegistries.PARTICLE_TYPES.getCodec().fieldOf("type").forGetter(data -> data.type),
                    Codec.FLOAT.fieldOf("x0").forGetter(data -> data.x0),
                    Codec.FLOAT.fieldOf("y0").forGetter(data -> data.y0),
                    Codec.FLOAT.fieldOf("z0").forGetter(data -> data.z0),
                    Codec.FLOAT.fieldOf("x1").forGetter(data -> data.x1),
                    Codec.FLOAT.fieldOf("y1").forGetter(data -> data.y1),
                    Codec.FLOAT.fieldOf("z1").forGetter(data -> data.z1)
            ).apply(instance,
                    (type, x0, y0, z0, x1, y1, z1) ->
                            new PointToPointParticleData<>((ParticleType<?>) type, x0, y0, z0, x1, y1, z1))
    );

    protected final ParticleType<T> type;
    protected final float x0;
    protected final float y0;
    protected final float z0;
    protected final float x1;
    protected final float y1;
    protected final float z1;

    public PointToPointParticleData(ParticleType<T> type, float x0, float y0, float z0, float x1, float y1, float z1) {
        this.type = type;
        this.x0 = x0;
        this.y0 = y0;
        this.z0 = z0;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
    }



    public float x0() {
        return x0;
    }

    public float y0() {
        return y0;
    }

    public float z0() {
        return z0;
    }

    public float x1() {
        return x1;
    }

    public float y1() {
        return y1;
    }

    public float z1() {
        return z1;
    }


    @Override
    public @NotNull ParticleType<T> getType() {
        return type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeFloat(x0);
        buf.writeFloat(y0);
        buf.writeFloat(z0);
        buf.writeFloat(x1);
        buf.writeFloat(y1);
        buf.writeFloat(z1);
    }

    @Override
    public @NotNull String writeToString() {
        return String.format("%s %.2f %.2f %.2f %.2f %.2f %.2f ",
                ForgeRegistries.PARTICLE_TYPES.getKey(getType()), x0, y0, z0, x1, y1, z1);
    }

    public static final Deserializer<PointToPointParticleData<?>> DESERIALIZER = new Deserializer<>() {
        @Override
        public @NotNull PointToPointParticleData<?> fromCommand(@NotNull ParticleType<PointToPointParticleData<?>> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float x0 = reader.readFloat();
            reader.expect(' ');
            float y0 = reader.readFloat();
            reader.expect(' ');
            float z0 = reader.readFloat();
            reader.expect(' ');
            float x1 = reader.readFloat();
            reader.expect(' ');
            float y1 = reader.readFloat();
            reader.expect(' ');
            float z1 = reader.readFloat();
            return new PointToPointParticleData<>(type, x0, y0, z0, x1, y1, z1);
        }

        @Override
        public @NotNull PointToPointParticleData<?> fromNetwork(@NotNull ParticleType<PointToPointParticleData<?>> type, FriendlyByteBuf buf) {
            float x0 = buf.readFloat();
            float y0 = buf.readFloat();
            float z0 = buf.readFloat();
            float x1 = buf.readFloat();
            float y1 = buf.readFloat();
            float z1 = buf.readFloat();

            return new PointToPointParticleData<>(type, x0, y0, z0, x1, y1, z1);
        }
    };

    public static class PointToPointParticleProvider implements ParticleProvider<PointToPointParticleData<?>> {
        private final SpriteSet sprites;

        public PointToPointParticleProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(@NotNull PointToPointParticleData data, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new PointToPointParticle(level, x, y, z, data, sprites);
        }
    }
}