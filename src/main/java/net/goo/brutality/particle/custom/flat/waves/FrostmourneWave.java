package net.goo.brutality.particle.custom.flat.waves;

import net.goo.brutality.particle.base.WaveParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

import static net.goo.brutality.util.ModResources.*;

public class FrostmourneWave extends WaveParticle {
    protected FrostmourneWave(ClientLevel world, double x, double y, double z, SpriteSet sprites) {
        super(world, x, y, z, sprites);
        this.radius = FROSTMOURNE_WAVE_RADIUS;
        this.growthDuration = FROSTMOURNE_WAVE_DURATION;
        this.sprites = sprites;
        this.growthSpeed = FROSTMOURNE_WAVE_SPEED;
        this.setSpriteFromAge(sprites);
    }


    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new FrostmourneWave(level, x, y, z, this.sprites);
        }
    }
}
