package net.goo.brutality.client.particle.custom;

import net.goo.brutality.client.particle.base.RotatingParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class CosmicParticle extends RotatingParticle {

    private final SpriteSet spriteSet;

    protected CosmicParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
        this.spriteSet = spriteSet;
        this.setParticleSpeed(
                0.05 * (level.random.nextFloat() - 0.5),
                0.05 * (level.random.nextFloat() - 0.5),
                0.05 * (level.random.nextFloat() - 0.5)
        );
        this.gravity = 0;
        this.quadSize *= Mth.nextFloat(level.random, 0.5F, 2F);
        this.setSpriteFromAge(spriteSet);
    }


    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(spriteSet);
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
            return new CosmicParticle(level, x, y, z,
                    xSpeed, ySpeed, zSpeed, this.sprites);
        }
    }

    @Override
    public int getLightColor(float partialTick) {
        return 15728880;
    }


}
