package net.goo.brutality.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ZapParticle extends TextureSheetParticle {

    protected ZapParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.friction = 1F; // Determines particle movement slowdown
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.quadSize *= 1;
        this.lifetime = level.random.nextIntBetweenInclusive(1,3);
        
        this.pickSprite(spriteSet);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public int getLightColor(float partialTick) {
        return 15728880;
    }


    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
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
            return new ZapParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}
