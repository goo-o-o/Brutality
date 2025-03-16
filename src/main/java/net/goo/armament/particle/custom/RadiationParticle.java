package net.goo.armament.particle.custom;

import net.goo.armament.util.ModResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.player.Player;

public class RadiationParticle extends TextureSheetParticle {
    float direction;
    protected RadiationParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet) {
        super(pLevel, pX, pY, pZ);
        this.setSpriteFromAge(spriteSet);
        this.lifetime = 30;
        this.quadSize *= 2;
        this.friction = 0.95F;
        this.direction = pLevel.random.nextFloat() - 0.5F;
    }


    @Override
    public void tick() {
        super.tick();
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        float yaw = player.getYRot();
        double forwardX = -Math.sin(Math.toRadians(yaw)); // X component of forward direction
        double forwardZ = Math.cos(Math.toRadians(yaw));  // Z component of forward direction

        double oscillation = Math.cos(this.age * this.direction) * 0.1; // Adjust the multiplier for speed/amplitude

        double leftX = -forwardZ; // X component of left direction
        double leftZ = forwardX;  // Z component of left direction

        double motionX = leftX * oscillation; // Left/right motion (x-axis)
        double motionZ = leftZ * oscillation; // Left/right motion (z-axis)

        double motionY = 0.08; // Adjust for upward speed
        this.move(motionX, motionY, motionZ);

    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    protected int getLightColor(float pPartialTick) {
        return ModResources.MAX_LIGHT;
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
            return new RadiationParticle(level, x, y, z, this.spriteSet);
        }
    }
}
