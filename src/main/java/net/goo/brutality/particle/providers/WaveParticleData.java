package net.goo.brutality.particle.providers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.goo.brutality.particle.base.WaveParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class WaveParticleData implements ParticleOptions {
    public static final Codec<WaveParticleData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ForgeRegistries.PARTICLE_TYPES.getCodec().fieldOf("type").forGetter(data -> data.type),
                    Codec.FLOAT.fieldOf("radius").forGetter(data -> data.radius),
                    Codec.INT.fieldOf("growthDuration").forGetter(data -> data.growthDuration)
            ).apply(instance, (type, radius, growthDuration) ->
                    new WaveParticleData((ParticleType<WaveParticleData>) type, radius, growthDuration))
    );

    private final ParticleType<WaveParticleData> type;
    private final float radius;
    private final int growthDuration;

    public WaveParticleData(ParticleType<WaveParticleData> type, float radius, int growthDuration) {
        this.type = type;
        this.radius = radius;
        this.growthDuration = growthDuration;
    }

    public float radius() {
        return radius;
    }

    public int growthDuration() {
        return growthDuration;
    }

    @Override
    public @NotNull ParticleType<WaveParticleData> getType() {
        return type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeFloat(radius);
        buf.writeInt(growthDuration);
    }

    @Override
    public @NotNull String writeToString() {
        return String.format("%s %.2f %d", ForgeRegistries.PARTICLE_TYPES.getKey(getType()), radius, growthDuration);
    }

    public static final ParticleOptions.Deserializer<WaveParticleData> DESERIALIZER = new ParticleOptions.Deserializer<>() {
        @Override
        public @NotNull WaveParticleData fromCommand(ParticleType<WaveParticleData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float radius = reader.readFloat();
            reader.expect(' ');
            int duration = reader.readInt();
            return new WaveParticleData(type, radius, duration);
        }

        @Override
        public @NotNull WaveParticleData fromNetwork(@NotNull ParticleType<WaveParticleData> type, FriendlyByteBuf buf) {
            return new WaveParticleData(type, buf.readFloat(), buf.readInt());
        }
    };

    public static class WaveParticleProvider implements ParticleProvider<WaveParticleData> {
        private final SpriteSet sprites;

        public WaveParticleProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(@NotNull WaveParticleData data, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            WaveParticle particle = new WaveParticle(level, x, y, z, data, sprites);
            particle.pickSprite(sprites);
            return particle;
        }
    }
}