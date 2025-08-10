package net.goo.brutality.particle.custom.flat.waves;

import net.goo.brutality.particle.base.WaveParticle;
import net.goo.brutality.particle.providers.WaveParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;

import static net.minecraft.client.renderer.LightTexture.FULL_BRIGHT;

public class CosmicWave extends WaveParticle {


    public CosmicWave(ClientLevel level, double x, double y, double z, WaveParticleData data, SpriteSet sprites) {
        super(level, x, y, z, data, sprites);
    }

    @Override
    protected int getLightColor(float pPartialTick) {
        return FULL_BRIGHT;
    }

    public static class Provider implements ParticleProvider<WaveParticleData> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        @Override
        public Particle createParticle(WaveParticleData data, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new CosmicWave(level, x, y, z, data, sprites);
        }
    }
}
