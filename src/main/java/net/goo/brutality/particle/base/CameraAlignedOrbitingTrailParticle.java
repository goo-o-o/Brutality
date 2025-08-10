package net.goo.brutality.particle.base;

import net.goo.brutality.Brutality;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class CameraAlignedOrbitingTrailParticle extends TrailParticle {
    private static final ResourceLocation TRAIL_TEXTURE = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/particle/circle_trail_particle.png");

    private final int entityId;
    private final float width;
    private final float height;
    private final float initialYRot;
    private final float rotateByAge;

    public CameraAlignedOrbitingTrailParticle(ClientLevel world, double x, double y, double z, float r, float g, float b, float a, float width, int entityId, int sampleCount, SpriteSet sprite) {
        super(world, x, y, z, r, g, b, a, width, entityId, sampleCount, sprite);
        this.entityId = entityId;
        this.gravity = 0;
        this.lifetime = 20 + this.random.nextInt(20);
        initialYRot = random.nextFloat() * 360F;
        rotateByAge = (10 + random.nextFloat() * 10F) * (random.nextBoolean() ? -1F : 1F);
        this.width = width;
        this.height = width;
        Vec3 vec3 = getOrbitPosition();
        this.x = this.xo = vec3.x;
        this.y = this.yo = vec3.y;
        this.z = this.zo = vec3.z;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
    }


    public Vec3 getEntityPosition(){
        Entity from = this.getFromEntity();
        if(from != null){
            return from.position();
        }
        return new Vec3(this.x, this.y, this.z);
    }


    public Entity getFromEntity() {
        return entityId == -1 ? null : level.getEntity(entityId);
    }

    public Vec3 getOrbitPosition(){
        Vec3 dinoPos = getEntityPosition();
        Vec3 vec3 = new Vec3(0, height, width).yRot((float)Math.toRadians(initialYRot + rotateByAge * age));
        return dinoPos.add(vec3);
    }

    public void tick() {
        super.tick();
        this.a = 1F - age / (float) lifetime;
        Vec3 vec3 = getOrbitPosition();
        this.x = vec3.x;
        this.y = vec3.y;
        this.z = vec3.z;

        Entity from = this.getFromEntity();
        if(from == null){
            remove();
        }

    }


}