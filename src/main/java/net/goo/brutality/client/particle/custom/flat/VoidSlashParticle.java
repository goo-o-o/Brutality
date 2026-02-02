package net.goo.brutality.client.particle.custom.flat;

import net.goo.brutality.client.particle.base.FlatParticle;
import net.goo.brutality.client.particle.providers.FlatParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VoidSlashParticle extends FlatParticle {

    public VoidSlashParticle(ClientLevel level, double x, double y, double z, FlatParticleData<?> data, SpriteSet sprites) {
        super(level, x, y, z, data, sprites);
        this.lifetime = 5;
        this.setSpriteFromAge(sprites);
    }



    public static class VoidSlashParticleProvider implements ParticleProvider<FlatParticleData<?>> {
        private final SpriteSet sprites;

        public VoidSlashParticleProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public @Nullable Particle createParticle(@NotNull FlatParticleData<?> data, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new VoidSlashParticle(pLevel, pX, pY, pZ, data, sprites);
        }
    }

}
