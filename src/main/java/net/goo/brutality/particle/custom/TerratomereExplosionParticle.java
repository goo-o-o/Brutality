package net.goo.brutality.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.client.renderer.LightTexture.FULL_BRIGHT;

public class TerratomereExplosionParticle extends TextureSheetParticle {
    SpriteSet spriteSet;

    protected TerratomereExplosionParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet) {
        super(level, x, y, z, 0, 0, 0);
        this.setSpriteFromAge(spriteSet);
        this.spriteSet = spriteSet;
        this.lifetime = 24;
        this.quadSize *= 15;
        this.friction = 0;
    }


    public void tick() {
        super.tick();
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    protected int getLightColor(float pPartialTick) {
        return FULL_BRIGHT;
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
            return new TerratomereExplosionParticle(level, x, y, z, this.spriteSet);
        }
    }

}
