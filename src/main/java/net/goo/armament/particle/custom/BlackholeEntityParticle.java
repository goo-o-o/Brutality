package net.goo.armament.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlackholeEntityParticle extends TextureSheetParticle {
    protected BlackholeEntityParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.friction = 1.5F; // Determines particle movement slowdown
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.quadSize *= (level.random.nextFloat() * 2); // Adjust particle size
        this.lifetime = 5; // Number of ticks the particle will live
        this.setSpriteFromAge(spriteSet);
        this.rCol = 0;
        this.gCol = 0;
        this.bCol = 0;

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
            return new BlackholeEntityParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

}
