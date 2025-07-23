package net.goo.brutality.particle.custom;

import net.mcreator.terramity.client.particle.KamehamehaParticleParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class DepthCrusherParticle extends KamehamehaParticleParticle {
    SpriteSet spriteSet;
    protected DepthCrusherParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(world, x, y, z, vx, vy, vz, spriteSet);
        this.spriteSet = spriteSet;
        this.setLifetime(15);
        this.setColor(0.078F, 0.078F, 0.33F);
    }

    @Override
    public void tick() {
        super.tick();

        this.quadSize *= 0.99F;
        this.setSpriteFromAge(spriteSet);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new DepthCrusherParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

}
