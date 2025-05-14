package net.goo.armament.particle.custom;

import net.goo.armament.util.ModUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static net.minecraft.client.renderer.LightTexture.FULL_BRIGHT;

@OnlyIn(Dist.CLIENT)
public class MurasamaParticle extends TextureSheetParticle {

    protected MurasamaParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.friction = 0.75F; // Determines particle movement slowdown
        this.xd = ModUtils.nextFloatBetweenInclusive(level.random, 0.125F, -0.125F);
        this.yd = ModUtils.nextFloatBetweenInclusive(level.random, 0.125F, -0.125F);
        this.zd = ModUtils.nextFloatBetweenInclusive(level.random, 0.125F, -0.125F);
        this.quadSize *= 4;
        this.lifetime = level.random.nextIntBetweenInclusive(10,15);
        
        this.pickSprite(spriteSet);
    }


    @Override
    public int getLightColor(float partialTick) {
        return FULL_BRIGHT;
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
            return new MurasamaParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}
