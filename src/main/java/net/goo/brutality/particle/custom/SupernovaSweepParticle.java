package net.goo.brutality.particle.custom;

import net.goo.brutality.particle.base.SweepParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

import static net.minecraft.client.renderer.LightTexture.FULL_BRIGHT;

public class SupernovaSweepParticle extends SweepParticle {

    SupernovaSweepParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pQuadSizeMultiplier, SpriteSet pSprites) {
        super(pLevel, pX, pY, pZ,0.0D, pSprites);
        this.lifetime = 4;
        this.quadSize = 2.25F - (float)pQuadSizeMultiplier * 0.5F;
        this.setSpriteFromAge(pSprites);
    }

    public int getLightColor(float pPartialTick) {
        return FULL_BRIGHT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites) {
            this.sprites = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new SupernovaSweepParticle(pLevel, pX, pY, pZ, pXSpeed, this.sprites);
        }
    }
}