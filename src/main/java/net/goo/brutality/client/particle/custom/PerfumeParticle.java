package net.goo.brutality.client.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class PerfumeParticle extends BaseAshSmokeParticle {
   protected PerfumeParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, float pQuadSizeMultiplier, SpriteSet pSprites) {
      super(pLevel, pX, pY, pZ, 0.1F, 0.1F, 0.1F, pXSpeed, pYSpeed, pZSpeed, pQuadSizeMultiplier, pSprites, 0.3F, 8, -0.1F, true);
      this.quadSize *= 15;
      this.setColor(1F, 0.5F, 0.5F);
   }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
      private final SpriteSet sprites;

      public Provider(SpriteSet pSprites) {
         this.sprites = pSprites;
      }

      public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
         return new PerfumeParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, 1.0F, this.sprites);
      }
   }
}