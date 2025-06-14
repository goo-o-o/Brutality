package net.goo.brutality.particle.custom;

import net.goo.brutality.util.ModUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class TerraParticle extends TextureSheetParticle {
    private final boolean rollingClockwise;
    protected float initialRollSpeed = 7.5F, rollInertia = 0.95F;

    protected TerraParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet) {
        super(level, x, y, z, 0, 0, 0);

        this.friction = 0.9F; // Determines particle movement slowdown
        this.lifetime = 50; // Number of ticks the particle will live
        this.quadSize *= ModUtils.nextFloatBetweenInclusive(random, 1, 3);
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

        this.rollingClockwise = level.random.nextBoolean();

        this.pickSprite(spriteSet); // Initial sprite based on age
    }


    public void tick() {
        super.tick();
        initialRollSpeed *= rollInertia;
        if (rollingClockwise) this.roll = initialRollSpeed;
        else this.roll = -initialRollSpeed;
        this.oRoll = this.roll;

        this.move(this.xd, this.yd, this.zd);

        if (this.age >= this.lifetime - 60 && this.quadSize > 0.01F) {
            this.quadSize *= 0.95F;
        } else this.remove();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    protected int getLightColor(float pPartialTick) {
        return 15728880;
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
