package net.goo.armament.particle.custom;

import net.goo.armament.particle.ArmaSweepParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static net.goo.armament.util.ModResources.MAX_LIGHT;

@OnlyIn(Dist.CLIENT)
public class SoulSweepParticle extends ArmaSweepParticle {

    public SoulSweepParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pQuadSizeMultiplier, SpriteSet pSprites) {
        super(pLevel, pX, pY, pZ, 0.0D, pSprites);
        this.sprites = pSprites;
        this.lifetime = 4;
//        float f = nextFloatBetweenInclusive(this.random, 0.75F, 1F);
        this.rCol = 1;
        this.gCol = 1;
        this.bCol = 1;
        this.quadSize = 3F - (float) pQuadSizeMultiplier * 0.5F;
        this.setSpriteFromAge(pSprites);
    }

    public int getLightColor(float pPartialTick) {
        return MAX_LIGHT;
    }


    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
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