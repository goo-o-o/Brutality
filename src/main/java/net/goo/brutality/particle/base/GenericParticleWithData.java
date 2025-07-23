package net.goo.brutality.particle.base;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.goo.brutality.registry.BrutalityModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;

import static net.minecraft.client.renderer.LightTexture.FULL_BRIGHT;

public class GenericParticleWithData extends TextureSheetParticle {
    protected SpriteSet sprites;
    protected float size;
    protected int EntityId;

    protected GenericParticleWithData(ClientLevel world, double x, double y, double z, SpriteSet sprites, int EntityId, float size) {
        super(world, x, y + 0.5, z, 0.0, 0.0, 0.0);
        this.quadSize = 1;
        this.setParticleSpeed(0D, 0D, 0D);
        this.EntityId = EntityId;
        this.lifetime = 20;
        this.size = size;
        this.sprites = sprites;
        this.setSpriteFromAge(sprites);
    }


    @Override
    public boolean shouldCull() {
        return false;
    }


    @Override
    protected int getLightColor(float pPartialTick) {
        return FULL_BRIGHT;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
    }

    public Entity getFromEntity() {
        return EntityId == -1 ? null : level.getEntity(EntityId);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public record ParticleData(int entityId, float size) implements ParticleOptions {
        public static final Deserializer<ParticleData> DESERIALIZER = new Deserializer<>() {
            public ParticleData fromCommand(ParticleType<ParticleData> type, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                int entityId = reader.readInt();
                reader.expect(' ');
                float size = reader.readFloat();
                return new ParticleData(entityId, size);
            }

            public ParticleData fromNetwork(ParticleType<ParticleData> type, FriendlyByteBuf buffer) {
                return new ParticleData(buffer.readInt(), buffer.readFloat());
            }
        };

        @Override
        public void writeToNetwork(FriendlyByteBuf buffer) {
            buffer.writeInt(entityId);
            buffer.writeFloat(size);
        }

        @Override
        public String writeToString() {
            return String.format(Locale.ROOT, "%s %d %.2f", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), entityId, size);
        }

        @Override
        public ParticleType<ParticleData> getType() {
            return BrutalityModParticles.GENERIC_PARTICLE_WITH_DATA.get();
        }

        public static Codec<ParticleData> CODEC(ParticleType<ParticleData> type) {
            return RecordCodecBuilder.create(instance -> instance.group(
                    Codec.INT.fieldOf("entityId").forGetter(ParticleData::entityId),
                    Codec.FLOAT.fieldOf("size").forGetter(ParticleData::size))
                    .apply(instance, ParticleData::new));
        }
    }

    public static class Provider implements ParticleProvider<ParticleData> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(ParticleData data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new GenericParticleWithData(level, x, y, z, this.spriteSet, data.entityId(), data.size());

        }
    }

}