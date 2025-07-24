package net.goo.brutality.particle.custom;

import net.goo.brutality.particle.base.RotatingParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class OiledParticle extends RotatingParticle {
    protected OiledParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
        this.gravity = 0.35F;
        this.setParticleSpeed(
                0.05 * (level.random.nextFloat() - 0.5),
                0.05 * (level.random.nextFloat() - 0.5),
                0.05 * (level.random.nextFloat() - 0.5)
        );
        this.pickSprite(spriteSet);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            RotatingParticle particle = new OiledParticle(level, x, y, z,
                    xSpeed, ySpeed, zSpeed, this.sprites);
            particle.pickSprite(this.sprites);
            return particle;
        }
    }
}
