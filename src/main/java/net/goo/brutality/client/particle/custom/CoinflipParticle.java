package net.goo.brutality.client.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class CoinflipParticle extends TextureSheetParticle {

    SpriteSet spriteSet;

    protected CoinflipParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet) {
        super(pLevel, pX, pY, pZ, 0, 0, 0);
        this.hasPhysics = false;
        this.spriteSet = spriteSet;
        this.quadSize = 0.25F;
        this.lifetime = 60;
        this.xd = 0;
        this.zd = 0;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected int getLightColor(float pPartialTick) {
        return LightTexture.FULL_BRIGHT;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.age < 10) {
            this.yd = 0.02D; // Medium float up
        }
        else if (this.age < 40) {
            // Slowly decelerate but keep a minimum upward drift
            this.yd = Math.max(0.005D, this.yd * 0.85D);
        } else {
            // Rapidly speed up at the end
            this.yd = Math.min(0.1D, this.yd + 0.02D);

            float fadeProgress = (float)(this.age - 40) / (this.lifetime - 40);
            float rawAlpha = 1.0f - fadeProgress;
            this.alpha = Math.max(0, rawAlpha * rawAlpha);

            if (this.alpha <= 0) this.remove();
        }
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
            CoinflipParticle particle = new CoinflipParticle(level, x, y, z, this.sprites);
            particle.pickSprite(this.sprites);
            return particle;
        }
    }
}
