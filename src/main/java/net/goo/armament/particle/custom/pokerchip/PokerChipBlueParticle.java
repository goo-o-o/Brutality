package net.goo.armament.particle.custom.pokerchip;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PokerChipBlueParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private double initialVelocity; // Adjust this for launch speed
    private double time = 0; // Track time elapsed

    protected PokerChipBlueParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet) {
        super(level, x, y, z, 0, 0, 0);

        this.sprites = spriteSet;
        this.friction = 1F; // Determines particle movement slowdown
        this.lifetime = 100; // Number of ticks the particle will live

        // Randomize initial angle for spherical dispersion
        double theta = random.nextDouble() * Math.PI; // Polar angle from 0 to PI (zenith angle)
        double phi = random.nextDouble() * 2 * Math.PI; // Azimuthal angle from 0 to 2*PI (around the azimuth)

        // Randomize the initial speed (magnitude)
        this.initialVelocity = (0.5 + random.nextDouble()) / 7.5; // Between 0.5 and 1.5 units/s

        // Convert spherical coordinates to Cartesian coordinates
        this.xd = initialVelocity * Math.sin(theta) * Math.cos(phi); // Horizontal component (X)
        this.yd = initialVelocity * Math.cos(theta); // Upward component (Z)
        this.zd = initialVelocity * Math.sin(theta) * Math.sin(phi); // Horizontal component (Z)

        this.setSpriteFromAge(spriteSet); // Initial sprite based on age
    }

    @Override
    public void tick() {
        super.tick();

        time += 0.4; // Increment time, adjust increment for control over animation speed

        // Update velocities influenced by gravity for Y direction
        this.yd = this.yd - (0.04 * time); // Apply gravity over time

        // Apply movement based on computed velocities
        this.move(this.xd, this.yd, this.zd);
        this.setSpriteFromAge(sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }


    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new PokerChipBlueParticle(level, x, y, z, this.spriteSet);
        }
    }

}
