package net.goo.brutality.client.particle.custom.trail;

import net.goo.brutality.client.particle.base.AbstractHeadedTrailParticle;
import net.goo.brutality.client.particle.providers.EntityIdParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;

public class HealingTrailParticle extends AbstractHeadedTrailParticle {
    private final Entity entity;

    protected HealingTrailParticle(EntityIdParticleData<?> data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.alpha = 1.0F;
        this.hasPhysics = false;
        entity = level.getEntity(data.getEntityId());
        this.lifetime = 1000;
        this.setColor(1, 0, 0);
    }

    @Override
    protected boolean shouldNotRenderHead() {
        return (trailPointer == -1 || (entity == null && age > sampleCount()));
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (entity != null && !entity.isRemoved()) {
            this.setPos(entity.getX(), entity.getY(0.5), entity.getZ());
            this.age = 1; // Keep age at 0 while entity exists
        } else {
            // Entity is gone, start aging to fade out
            this.age++;
            // Remove once the trail has fully dispersed
            if (this.age > this.sampleCount()) {
                this.remove();
            }
        }
        this.tickTrail();
    }

    private static final int DARK_RED = FastColor.ARGB32.color(255, 119, 9, 0);
    private static final int RED = FastColor.ARGB32.color(255, 255, 0, 0);

    @Override
    public int getTrailColor(float percentageFromHead) {
        return FastColor.ARGB32.lerp(percentageFromHead, RED, DARK_RED);
    }


    public static class Provider implements ParticleProvider<EntityIdParticleData<?>> {
        public Particle createParticle(EntityIdParticleData<?> data, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            HealingTrailParticle particle = new HealingTrailParticle(data, worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.trailR = 255;
            particle.trailG = 0;
            particle.trailB = 0;
            return particle;
        }
    }
}
