package net.goo.arma.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SupernovaSwordParticle extends TextureSheetParticle {
    protected SupernovaSwordParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.friction = 0.8F; // Determines particle movement slowdown
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.quadSize *= (level.random.nextFloat() * 2); // Adjust particle size
        this.lifetime = 60; // Number of ticks the particle will live
        this.setSpriteFromAge(spriteSet);

    }

    @Override
    public void tick() {
        super.tick();
        fadeOut();
    }

    private void fadeOut() {
        if (age <= 40) {
        // Normalized RGB values of start and end colors
        float startR = 240 / 255.0F, startG = 220 / 255.0F, startB = 160 / 255.0F;
        float endR = 100 / 255.0F, endG = 80 / 255.0F, endB = 160 / 255.0F;

        float progress = (float) age / 40.0F;
        this.rCol = startR + (endR - startR) * progress;
        this.gCol = startG + (endG - startG) * progress;
        this.bCol = startB + (endB - startB) * progress;

        this.alpha = 1.0F; // Full opacity during color transition

        } else if (age <= 60) {
        // Phase 2: Fade out (alpha from 1 to 0)
        this.alpha = 1.0F - ((float) (age - 40) / 20.0F);

        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT; // Translucent particles
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
            return new SupernovaSwordParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

    @Override
    public int getLightColor(float partialTick) {
        int light = super.getLightColor(partialTick);
        // If you want the particle to emit light, you can manipulate the light value here.
        // A simple example: return full brightness when the particle is active.
        return 15728880; // Full bright light (0xF0F0F0)
    }

}
