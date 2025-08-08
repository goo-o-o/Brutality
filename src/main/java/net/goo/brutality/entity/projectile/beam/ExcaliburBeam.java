package net.goo.brutality.entity.projectile.beam;

import net.goo.brutality.entity.base.SwordBeam;
import net.goo.brutality.particle.base.AbstractWorldAlignedTrailParticle;
import net.goo.brutality.registry.BrutalityModParticles;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ExcaliburBeam extends SwordBeam {
    private final Set<UUID> hitEntities = new HashSet<>(); // Track entities that have been hit

    public ExcaliburBeam(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level, 45, 65);
    }

    @Override
    public int getLifespan() {
        return 50;
    }

    @Override
    public float getDamage() {
        return 15F;
    }

    @Override
    public float getInertia() {
        return 1.075F;
    }

    @Override
    public int getPierceCap() {
        return 0;
    }


    @Override
    public float getRenderScale() {
        return 5;
    }


    @Override
    public SimpleParticleType getHitParticle() {
        return BrutalityModParticles.SPARKLE_PARTICLE.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {

    }

    private boolean trailSpawned = false;


    @Override
    public void tick() {
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);

        if (!trailSpawned && level().isClientSide()) {
            Vec3 moveVec = this.getDeltaMovement();

            moveVec = moveVec.normalize();

            float yaw = (float) Math.atan2(-moveVec.z, moveVec.x);
            float pitch = (float) Math.asin(moveVec.y);

            this.level().addParticle((new AbstractWorldAlignedTrailParticle.OrbData(1F, 1F, 0, this.getBbWidth(), this.getId(), pitch, yaw, getRandomRollRadians(), "sword", 5)), this.getX(), this.getY() + getBbHeight() / 2, this.getZ(), 0, 0, 0);
            trailSpawned = true;
        }

        if (hitresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitresult)) {
            this.onHit(hitresult);
        }

        AABB hitbox = this.getBoundingBox();
        List<Entity> entities = this.level().getEntities(this, hitbox, this::canHitEntity);

        if (!entities.isEmpty()) {
            for (Entity entity : entities) {
                UUID entityId = entity.getUUID();

                // Check if the entity has already been hit
                if (!hitEntities.contains(entityId)) {
                    // Apply damage to the entity
                    entity.hurt(this.damageSources().playerAttack((Player) this.getOwner()), this.getDamage());
                    // Mark the entity as hit
                    hitEntities.add(entityId);
                }
            }
        }

        level().addParticle(BrutalityModParticles.SPARKLE_PARTICLE.get(), this.getX(), this.getY() + getBbHeight() / 2, this.getZ(), 1, 0, 0);

        if (this.getDeltaMovement().length() < 0.1) {
            this.hitEntities.clear();
            this.discard();
        }

        super.tick();
    }


}
