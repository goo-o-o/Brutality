package net.goo.brutality.client.particle.custom;

import net.goo.brutality.client.particle.base.RotatingParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class YinYangParticle extends RotatingParticle {
    SpriteSet sprites;

    protected YinYangParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        this.gravity = 0;
        this.sprites = sprites;
        this.setParticleSpeed(
                0.025 * (level.random.nextFloat() - 0.5),
                0.025 * (level.random.nextFloat() - 0.5),
                0.025 * (level.random.nextFloat() - 0.5)
        );
        this.pickSprite(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(sprites);
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
            RotatingParticle particle = new YinYangParticle(level, x, y, z,
                    xSpeed, ySpeed, zSpeed, this.sprites);
            particle.setSpriteFromAge(this.sprites);
            return particle;
        }
    }
}
