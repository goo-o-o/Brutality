package net.goo.brutality.client.particle.custom.flat;

import net.goo.brutality.client.particle.base.PointToPointParticle;
import net.goo.brutality.client.particle.providers.PointToPointParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LightTexture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StygianStepParticle extends PointToPointParticle {

    public StygianStepParticle(ClientLevel level, double x, double y, double z, PointToPointParticleData<?> data, SpriteSet sprites) {
        super(level, x, y, z, data, sprites);
        this.lifetime = 120;
    }

    public static class Provider implements ParticleProvider<PointToPointParticleData<?>> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public @Nullable Particle createParticle(@NotNull PointToPointParticleData<?> data, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            StygianStepParticle particle = new StygianStepParticle(pLevel, pX, pY, pZ, data, sprites);
            particle.pickSprite(sprites);
            return particle;
        }
    }

    @Override
    protected int getLightColor(float partialTick) {
        return LightTexture.FULL_SKY;
    }
}
