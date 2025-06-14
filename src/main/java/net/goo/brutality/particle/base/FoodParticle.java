package net.goo.brutality.particle.base;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class FoodParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final float rotationSpeed;

    protected FoodParticle(ClientLevel level, double x, double y, double z,
                           double xSpeed, double ySpeed, double zSpeed,
                           SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = spriteSet;

        this.lifetime = 40 + random.nextInt(20);
        this.quadSize = 0.2f + random.nextFloat() * 0.15f;
        this.rotationSpeed = (random.nextFloat() - 0.5f) * 0.1f;

        this.setSpriteFromAge(spriteSet);

        this.xd = xSpeed * (0.8 + random.nextDouble() * 0.4);
        this.yd = ySpeed * (0.5 + random.nextDouble());
        this.zd = zSpeed * (0.8 + random.nextDouble() * 0.4);
        this.gravity = 0.25F;
    }

    @Override
    public void tick() {
        super.tick();

        this.oRoll = this.roll;
        this.roll += this.rotationSpeed;

        this.alpha = 1.0f - ((float)this.age / (float)this.lifetime);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            FoodParticle particle = new FoodParticle(level, x, y, z,
                    xSpeed, ySpeed, zSpeed, this.sprites);
            particle.pickSprite(this.sprites);
            return particle;
        }
    }
}