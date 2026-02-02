package net.goo.brutality.client.particle.custom;

import net.goo.brutality.client.particle.base.GenericCircleParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;

public class HealingParticle extends GenericCircleParticle {


    protected HealingParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);

        this.lifetime = 10;
        this.rCol = 157;
        this.gCol = 0;
        this.bCol = 0;
        this.quadSize = 0.05F;

    }

    @Override
    public void tick() {
        super.tick();
        if (!this.removed) {
            float ageProgress = (float) this.age / (float) this.lifetime;
            float startSize = 0.20f;
            float endSize = 0.025f;
            this.quadSize = startSize + (endSize - startSize) * ageProgress;

            if (this.age > this.lifetime / 2) {
                float fadeProgress = (float) (this.age - (this.lifetime / 2)) / (this.lifetime / 2f);
                this.alpha = Math.max(0f, 1f - fadeProgress);
            }
        }
    }

    @Override
    protected int getLightColor(float pPartialTick) {
        return LightTexture.FULL_BRIGHT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new HealingParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

}
