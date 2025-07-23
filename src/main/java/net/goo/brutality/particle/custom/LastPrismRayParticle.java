package net.goo.brutality.particle.custom;

import net.mcreator.terramity.client.particle.KamehamehaParticleParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.getCyclingColors;
import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.intToRgb;

public class LastPrismRayParticle extends KamehamehaParticleParticle {
    SpriteSet spriteSet;
    protected LastPrismRayParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(world, x, y, z, vx, vy, vz, spriteSet);
        this.spriteSet = spriteSet;
    }


    int[][] colors = new int[][]{
            {255, 0, 0},
            {255, 127, 0},
            {255, 255, 0},
            {0, 255, 0},
            {0, 0, 255},
            {75, 0, 130},
            {148, 0, 211}
    };

    @Override
    public void tick() {
        super.tick();

        int[] currentColor = intToRgb(getCyclingColors(1, colors));

        this.setColor((float) currentColor[0] / 255, (float) currentColor[1] / 255, (float) currentColor[2] / 255);

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
