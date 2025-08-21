package net.goo.brutality.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class BloodParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;

    protected BloodParticle(ClientLevel level, double x, double y, double z,
                            double xSpeed, double ySpeed, double zSpeed,
                            SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.lifetime = 40 + random.nextInt(20);
        this.quadSize = 0.2f + random.nextFloat() * 0.15f;
        this.spriteSet = spriteSet;

        this.xd = xSpeed * ((random.nextDouble() - 0.5F) * 0.4);
        this.yd = ySpeed * (random.nextDouble() - 0.25F);
        this.zd = zSpeed * ((random.nextDouble() - 0.5F) * 0.4);
        this.gravity = 0.65F;

        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(spriteSet);

        this.oRoll = this.roll;
        this.roll = (float) ((float) Math.atan2(this.xd, this.yd) + Math.PI);

        this.alpha = 1.0f - ((float)this.age / (float)this.lifetime);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
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
            return new BloodParticle(level, x, y, z,
                    xSpeed, ySpeed, zSpeed, this.sprites);
        }
    }
}