package net.goo.brutality.particle.custom;

import net.goo.brutality.particle.base.RotatingParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PepperParticle extends RotatingParticle {
    protected PepperParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
        this.gravity = 0.35F;
        this.setParticleSpeed(
                0.2 * (level.random.nextFloat() - 0.5),
                0.2 * (level.random.nextFloat() - 0.5),
                0.2 * (level.random.nextFloat() - 0.5)
        );
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
            RotatingParticle particle = new PepperParticle(level, x, y, z,
                    xSpeed, ySpeed, zSpeed, this.sprites);
            particle.pickSprite(this.sprites);
            return particle;
        }
    }
}
