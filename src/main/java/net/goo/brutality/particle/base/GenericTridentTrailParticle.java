package net.goo.brutality.particle.base;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class GenericTridentTrailParticle extends AbstractWorldAlignedTrailParticle {


    public GenericTridentTrailParticle(ClientLevel world, double x, double y, double z, float r, float g, float b, float a, float width, int entityId, int sampleCount, SpriteSet sprite) {
        super(world, x, y, z, r, g, b, a, width, entityId, sampleCount, sprite);
    }

    @Override
    public Vec3 getEntityCenter() {
        Entity from = this.getFromEntity();
        if (from != null) {
            Vec3 movement = getRenderedDirection();

            return from.position().add(movement.scale(-1F));
        }
        return new Vec3(this.x, this.y - 0.25, this.z);
    }

    public Vec3 getRenderedDirection() {
        Entity owner = this.getFromEntity();
        if (owner != null) {
            Vec3 movement = owner.getDeltaMovement();
            if (movement.lengthSqr() < 0.0001) { // Small value to account for floating point errors
                return new Vec3(0, 0, 0); // Not moving
            }
            return movement.normalize();
        }
        return new Vec3(this.x, this.y, this.z);
    }

}