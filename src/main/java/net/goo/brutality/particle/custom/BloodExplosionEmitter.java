package net.goo.brutality.particle.custom;

import net.goo.brutality.registry.BrutalityModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;

public class BloodExplosionEmitter extends NoRenderParticle {
    private int life;

    BloodExplosionEmitter(ClientLevel pLevel, double pX, double pY, double pZ) {
        super(pLevel, pX, pY, pZ, 0.0D, 0.0D, 0.0D);
    }

    public void tick() {
        int lifeTime = 8;
        for (int i = 0; i < 6; ++i) {
            double d0 = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 4.0D;
            double d1 = this.y + (this.random.nextDouble() - this.random.nextDouble()) * 4.0D;
            double d2 = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 4.0D;
            this.level.addParticle(BrutalityModParticles.BLOOD_EXPLOSION_PARTICLE.get(), d0, d1, d2, (float) this.life / (float) lifeTime, 0.0D, 0.0D);
            this.level.addParticle(BrutalityModParticles.BLOOD_PARTICLE.get(), d0, d1, d2, 1,1,1);
        }

        ++this.life;
        if (this.life >= lifeTime) {
            this.remove();
        }

    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new BloodExplosionEmitter(pLevel, pX, pY, pZ);
        }
    }
}