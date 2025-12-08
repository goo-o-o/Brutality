package net.goo.brutality.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.client.renderer.LightTexture.FULL_BRIGHT;

public class ShadowAssassinParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    private float angularVelocity;
    private final float angularAcceleration;


    protected ShadowAssassinParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(world, x, y, z);
        this.spriteSet = spriteSet;
        this.quadSize *= level.random.nextIntBetweenInclusive(2, 8);
        this.setSize(quadSize, quadSize);
        this.lifetime = level.random.nextIntBetweenInclusive(20, 35);
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
        this.angularVelocity = 0.1F;
        this.angularAcceleration = 0.01F;
        this.setSpriteFromAge(spriteSet);
    }

    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    protected int getLightColor(float pPartialTick) {
        return FULL_BRIGHT;
    }

    public void tick() {
        super.tick();
        this.oRoll = this.roll;
        this.roll += this.angularVelocity;
        this.angularVelocity += this.angularAcceleration;
        this.setSpriteFromAge(spriteSet);
        if (this.age >= this.lifetime - 60 && this.quadSize > 0.01F) {
            this.quadSize *= 0.95F;
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ShadowAssassinParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}
