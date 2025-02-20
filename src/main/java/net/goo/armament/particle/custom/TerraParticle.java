package net.goo.armament.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class TerraParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final boolean rollRot;
    private int tickCount;

    protected TerraParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet) {
        super(level, x, y, z, 0, 0, 0);

        this.sprites = spriteSet;
        this.friction = 0.9F; // Determines particle movement slowdown
        this.lifetime = 50; // Number of ticks the particle will live

        // Randomize initial angle for spherical dispersion
        double theta = random.nextDouble() * Math.PI; // Polar angle from 0 to PI (zenith angle)
        double phi = random.nextDouble() * 2 * Math.PI; // Azimuthal angle from 0 to 2*PI (around the azimuth)

        // Randomize the initial speed (magnitude)
        // Adjust this for launch speed
        double initialVelocity = (0.5 + random.nextDouble()) / 7.5; // Between 0.5 and 1.5 units/s

        // Convert spherical coordinates to Cartesian coordinates
        this.xd = initialVelocity * Math.sin(theta) * Math.cos(phi); // Horizontal component (X)
        this.yd = initialVelocity * Math.cos(theta); // Upward component (Z)
        this.zd = initialVelocity * Math.sin(theta) * Math.sin(phi); // Horizontal component (Z)

        this.rollRot = level.random.nextBoolean();

        this.setSpriteFromAge(spriteSet); // Initial sprite based on age
    }

    @Override
    public void tick() {
        super.tick();
        tickCount++;

        // Calculate alpha for fading out
        if (tickCount > lifetime) {
            // Fade from 1.0 (fully visible) to 0.0 (invisible)
            float alpha = 1.0f - (tickCount / (float) lifetime);
            this.setAlpha(alpha);
        } else {
            // If lifetime exceeds, set alpha to 0
            this.setAlpha(0.0f);
        }

        // Update the position of the particle
        this.move(this.xd, this.yd, this.zd);
        this.pickSprite(sprites);

        // Rolling clockwise
        float rollRot = 0.1f / (tickCount + 1); // Speed of rolling
        if (this.rollRot) {
            this.roll += rollRot;
        } else {
            this.roll -= rollRot;
        }
        this.roll %= 360; // Keep the roll angle within 0-360 degrees
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
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
            return new TerraParticle(level, x, y, z, this.spriteSet);
        }
    }

}
