package net.goo.brutality.client.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.client.renderer.LightTexture.FULL_BRIGHT;

public class BiomechReactorParticle extends TextureSheetParticle {
SpriteSet spriteSet;
    protected BiomechReactorParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet) {
        super(level, x, y, z, 0, 0, 0);
        this.setSpriteFromAge(spriteSet);
        this.spriteSet = spriteSet;
        RandomSource random = level.random;
        this.lifetime = 28;
        this.quadSize *= 100F;
        this.friction = 0;
        this.roll = Mth.randomBetweenInclusive(random, 0, 180);
        this.oRoll = this.roll;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public boolean shouldCull() {
        return false;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
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
            return new BiomechReactorParticle(level, x, y, z, this.spriteSet);
        }
    }

}
