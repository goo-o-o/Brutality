package net.goo.armament.particle.custom;

import net.goo.armament.particle.base.ArmaSweepParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static net.minecraft.client.renderer.LightTexture.FULL_BRIGHT;

@OnlyIn(Dist.CLIENT)
public class SoulSweepParticle extends ArmaSweepParticle {

    public SoulSweepParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pQuadSizeMultiplier, SpriteSet pSprites) {
        super(pLevel, pX, pY, pZ, 0.0D, pSprites);
        this.lifetime = 4;
        this.quadSize = 3F - (float) pQuadSizeMultiplier * 0.5F;
        this.setSpriteFromAge(pSprites);
    }

    public int getLightColor(float pPartialTick) {
        return FULL_BRIGHT;
    }



    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites) {
            this.sprites = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new SoulSweepParticle(pLevel, pX, pY, pZ, pXSpeed, this.sprites);
        }
    }
}