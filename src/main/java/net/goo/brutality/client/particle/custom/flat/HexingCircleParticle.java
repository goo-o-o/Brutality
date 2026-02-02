package net.goo.brutality.client.particle.custom.flat;

import net.goo.brutality.client.particle.base.FlatParticle;
import net.goo.brutality.client.particle.providers.FlatParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HexingCircleParticle extends FlatParticle {

    public HexingCircleParticle(ClientLevel level, double x, double y, double z, FlatParticleData<?> data, SpriteSet sprites) {
        super(level, x, y, z, data, sprites);
        this.lifetime = 120;
    }

    public static class Provider implements ParticleProvider<FlatParticleData<?>> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public @Nullable Particle createParticle(@NotNull FlatParticleData<?> data, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            HexingCircleParticle particle = new HexingCircleParticle(pLevel, pX, pY, pZ, data, sprites);
            particle.pickSprite(sprites);
            return particle;
        }
    }

}
