package net.goo.brutality.particle.custom;

import net.goo.brutality.particle.base.TrailParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.world.entity.Entity;

public class CelestialStarboardTrailParticle extends TrailParticle {


    public CelestialStarboardTrailParticle(ClientLevel world, double x, double y, double z, float r, float g, float b, float a, float width, int entityId, int sampleCount, SpriteSet sprite) {
        super(world, x, y, z, r, g, b, a, width, entityId, sampleCount, sprite);
        this.lifetime = 2000;
    }


    public void tick() {
        super.tick();
        float fade = 1F - age / (float) lifetime;
        this.a = fade * 2F;
        Entity from = this.getFromEntity();

        if (from == null) {
            remove();
        } else {
            this.y = from.getY();
            if (from.onGround()) this.remove();
        }

    }

}