package net.goo.brutality.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class SupernovaParticle extends TextureSheetParticle {

    private final SpriteSet spriteSet;

    protected SupernovaParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.friction = 0.8F; // Determines particle movement slowdown
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.quadSize *= (level.random.nextFloat() * 3); // Adjust particle size
        this.lifetime = 60; // Number of ticks the particle will live
        this.setSpriteFromAge(spriteSet);
        this.spriteSet = spriteSet;

        double theta = random.nextDouble() * Math.PI; // Polar angle from 0 to PI (zenith angle)
        double phi = random.nextDouble() * 2 * Math.PI; // Azimuthal angle from 0 to 2*PI (around the azimuth)

        // Randomize the initial speed (magnitude)
        double initialVelocity = (0.5 + random.nextDouble()) / 2; // Between 0.5 and 1.5 units/s

        // Convert spherical coordinates to Cartesian coordinates
        this.xd = initialVelocity * Math.sin(theta) * Math.cos(phi); // Horizontal component (X)
        this.yd = initialVelocity * Math.cos(theta); // Upward component (Z)
        this.zd = initialVelocity * Math.sin(theta) * Math.sin(phi); // Horizontal component (Z)

    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE; // Translucent particles
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
            return new SupernovaParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

    @Override
    public int getLightColor(float partialTick) {
        return 15728880;
    }


}
