package net.goo.brutality.client.particle.custom;

import net.goo.brutality.util.ColorUtils;
import net.goo.brutality.util.ModResources;
import net.mcreator.terramity.client.particle.KamehamehaParticleParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.FastColor;

public class LastPrismRayParticle extends KamehamehaParticleParticle {
    SpriteSet spriteSet;

    protected LastPrismRayParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(world, x, y, z, vx, vy, vz, spriteSet);
        this.spriteSet = spriteSet;
    }


    @Override
    public void tick() {
        super.tick();

        int currentColor = ColorUtils.getCyclingColor(0.1F, ModResources.rainbowColor);

        this.setColor(
                FastColor.ARGB32.red(currentColor) / 255F,
                FastColor.ARGB32.green(currentColor) / 255F,
                FastColor.ARGB32.blue(currentColor) / 255F);

        this.setSpriteFromAge(spriteSet);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new LastPrismRayParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

}
