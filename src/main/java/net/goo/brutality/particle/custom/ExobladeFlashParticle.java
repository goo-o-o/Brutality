package net.goo.brutality.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.client.renderer.LightTexture.FULL_BRIGHT;

public class ExobladeFlashParticle extends TextureSheetParticle {

    protected ExobladeFlashParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet) {
        super(level, x, y, z, 0, 0, 0);
        this.pickSprite(spriteSet);
        RandomSource random = level.random;
        this.lifetime = 10;
        this.quadSize = 0.3F;
        this.friction = 0;
        this.roll = Mth.randomBetweenInclusive(random, 0, 180);
        this.oRoll = this.roll;
        this.setAlpha(1);
        this.setColor(Mth.nextFloat(random, 0.5F, 1F),
                Mth.nextFloat(random, 0.5F, 1F),
                Mth.nextFloat(random, 0.5F, 1F));
    }


    public void tick() {
        super.tick();
        this.quadSize += (lifetime - age) * 0.1F;
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
            return new ExobladeFlashParticle(level, x, y, z, this.spriteSet);
        }
    }

}
