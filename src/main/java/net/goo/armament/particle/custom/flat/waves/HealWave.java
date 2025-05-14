package net.goo.armament.particle.custom.flat.waves;

import net.goo.armament.particle.base.WaveParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static net.goo.armament.util.ModResources.*;

public class HealWave extends WaveParticle {
    protected HealWave(ClientLevel world, double x, double y, double z, SpriteSet sprites) {
        super(world, x, y, z, sprites);
        this.radius = HEAL_WAVE_RADIUS;
        this.growthDuration = HEAL_WAVE_DURATION;
        this.sprites = sprites;
        this.growthSpeed = HEAL_WAVE_SPEED;
        this.setSpriteFromAge(sprites);
    }


    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new HealWave(level, x, y, z, this.sprites);
        }
    }
}
