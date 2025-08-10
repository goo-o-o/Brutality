package net.goo.brutality.particle.providers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.goo.brutality.particle.base.TrailParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class TrailParticleData implements ParticleOptions {
    public static final Codec<TrailParticleData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ForgeRegistries.PARTICLE_TYPES.getCodec().fieldOf("type").forGetter(data -> data.type),
                    Codec.FLOAT.fieldOf("r").forGetter(data -> data.r),
                    Codec.FLOAT.fieldOf("g").forGetter(data -> data.g),
                    Codec.FLOAT.fieldOf("b").forGetter(data -> data.b),
                    Codec.FLOAT.fieldOf("a").forGetter(data -> data.a),
                    Codec.FLOAT.fieldOf("width").forGetter(data -> data.width),
                    Codec.INT.fieldOf("entityId").forGetter(data -> data.entityId),
                    Codec.INT.fieldOf("sampleCount").forGetter(data -> data.sampleCount)
            ).apply(instance, (type, r, g, b, a,width, entityId, sampleCount) ->
                    new TrailParticleData((ParticleType<TrailParticleData>) type, r, g, b, a, width, entityId, sampleCount))
    );


    private final ParticleType<TrailParticleData> type;
    private final float r, g, b, a, width;
    private final int entityId, sampleCount;


    public TrailParticleData(ParticleType<TrailParticleData> type, float r, float g, float b, float a, float width, int entityId, int sampleCount) {
        this.type = type;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.width = width;
        this.entityId = entityId;
        this.sampleCount = sampleCount;
    }



    public float getR() {
        return r;
    }
    public float getG() {
        return g;
    }
    public float getB() {
        return b;
    }
    public float getA() {
        return a;
    }
    public float getWidth() {
        return width;
    }
    public int getEntityId() {
        return entityId;
    }
    public int getSampleCount() {
        return sampleCount;
    }


    @Override
    public @NotNull ParticleType<TrailParticleData> getType() {
        return type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeFloat(r);
        buf.writeFloat(g);
        buf.writeFloat(b);
        buf.writeFloat(a);
        buf.writeFloat(width);
        buf.writeInt(entityId);
        buf.writeInt(sampleCount);
    }

    @Override
    public @NotNull String writeToString() {
        return String.format("%s %.2f %.2f %.2f %.2f %.2f %d %d", ForgeRegistries.PARTICLE_TYPES.getKey(getType()), r, g, b, a, width, entityId, sampleCount);
    }

    public static final Deserializer<TrailParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public @NotNull TrailParticleData fromCommand(ParticleType<TrailParticleData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float r = reader.readFloat();
            reader.expect(' ');
            float g = reader.readFloat();
            reader.expect(' ');
            float b = reader.readFloat();
            reader.expect(' ');
            float a = reader.readFloat();
            reader.expect(' ');
            float width = reader.readFloat();
            reader.expect(' ');
            int entityId = reader.readInt();
            reader.expect(' ');
            int sampleCount = reader.readInt();

            return new TrailParticleData(type, r, g, b, a, width, entityId, sampleCount);
        }

        @Override
        public @NotNull TrailParticleData fromNetwork(@NotNull ParticleType<TrailParticleData> type, FriendlyByteBuf buf) {
            return new TrailParticleData(type, buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readInt(), buf.readInt());
        }
    };

    public static class TrailParticleProvider implements ParticleProvider<TrailParticleData> {
        private final SpriteSet sprite;

        public TrailParticleProvider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(@NotNull TrailParticleData data, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new TrailParticle(level, x, y, z, data.r, data.g, data.b, data.a, data.width, data.entityId, data.sampleCount, sprite);
        }
    }
}