package net.goo.armament.particle.custom;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.goo.armament.client.ArmaRenderTypes;
import net.goo.armament.util.helpers.EnvironmentColorManager;
import net.goo.armament.particle.base.GenericParticleWithData;
import net.goo.armament.registry.ModParticles;
import net.goo.armament.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;

public class ExplosionAmbientParticle extends GenericParticleWithData {
    private final float initialSize;
    protected SpriteSet sprites;
    protected float size;
    protected int EntityId;
    private boolean colorBeenReset = false, firstUpdate = true;

    protected ExplosionAmbientParticle(ClientLevel world, double x, double y, double z, SpriteSet sprites, int EntityId, float size) {
        super(world, x, y, z, sprites, EntityId, size);
        this.setParticleSpeed(0D, 0D, 0D);
        this.EntityId = EntityId;
        this.lifetime = 200;
        this.quadSize = size;
        this.initialSize = size;
        this.sprites = sprites;
        this.setSpriteFromAge(sprites);
    }

    private float baseScale; // Store the original scale
    private float flickerTimer = 0;
    private final float flickerSpeed = 0.15f; // Controls flicker speed
    private final float flickerIntensity = 0.2f; // Controls how strong the flicker is (0-1)

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);

        if (this.isAlive()) {
            float timeScale = 1 - (float) this.age / this.lifetime;
            // Apply scale with flicker effect
            this.quadSize = initialSize * ModUtils.nextFloatBetweenInclusive(level.random, 0.9F, 1.1F) * timeScale;

            if (level.isClientSide && age % (firstUpdate ? 1 : 10) == 0) {
                firstUpdate = false;
                LocalPlayer player = Minecraft.getInstance().player;
                if (player != null) {
                    double distanceSq = Math.sqrt(player.distanceToSqr(this.getPos()));

                    if (distanceSq < (this.quadSize * 2) + 20) {
                        EnvironmentColorManager.setColor(EnvironmentColorManager.ColorType.SKY, 255, 140, 0); // Orange
                        EnvironmentColorManager.setColor(EnvironmentColorManager.ColorType.FOG, 0, 0, 0);  // Black fog
                    } else {
                        EnvironmentColorManager.resetColor(EnvironmentColorManager.ColorType.SKY);
                        EnvironmentColorManager.resetColor(EnvironmentColorManager.ColorType.FOG);
                    }
                }
            }
        } else if (!isAlive() && !this.colorBeenReset) {
            EnvironmentColorManager.resetAllColors();
            this.colorBeenReset = true;
        }
    }

    public Entity getFromEntity() {
        return EntityId == -1 ? null : level.getEntity(EntityId);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ArmaRenderTypes.PARTICLE_SHEET_TRANSLUCENT_NO_CULL;
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
            return ModParticles.EXPLOSION_AMBIENT_PARTICLE.get();
        }

        public static Codec<ParticleData> CODEC(ParticleType<ParticleData> type) {
            return RecordCodecBuilder.create(instance -> instance.group(
                            Codec.INT.fieldOf("entityId").forGetter(ParticleData::entityId),
                            Codec.FLOAT.fieldOf("size").forGetter(ParticleData::size))
                    .apply(instance, ParticleData::new));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<ParticleData> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(ParticleData data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ExplosionAmbientParticle(level, x, y, z, this.spriteSet, data.entityId(), data.size());

        }
    }

}