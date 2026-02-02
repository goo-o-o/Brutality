package net.goo.brutality.client.particle.custom.flat;

import net.goo.brutality.client.particle.base.FlatParticle;
import net.goo.brutality.client.particle.providers.FlatParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MurasamaSlashParticle extends FlatParticle {

    public MurasamaSlashParticle(ClientLevel level, double x, double y, double z, FlatParticleData<?> data, SpriteSet sprites) {
        super(level, x, y, z, data, sprites);
        this.lifetime = 8;
        this.setSpriteFromAge(sprites);
    }


    @Override
    public void tick() {
        super.tick();
        if (relatedEntity != null) {
            this.setPos(relatedEntity.getX() + xOffset, relatedEntity.getY() + yOffset, relatedEntity.getZ() + zOffset);
        }
    }

    public static class MurasamaSlashParticleProvider implements ParticleProvider<FlatParticleData<?>> {
        private final SpriteSet sprites;

        public MurasamaSlashParticleProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public @Nullable Particle createParticle(@NotNull FlatParticleData<?> data, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new MurasamaSlashParticle(pLevel, pX, pY, pZ, data, sprites);
        }
    }

}
