package net.goo.brutality.common.entity.projectile.trident.physics_projectile;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.common.entity.base.BrutalityAbstractPhysicsThrowingProjectile;
import net.goo.brutality.common.entity.base.BrutalityAbstractThrowingProjectile;
import net.goo.brutality.common.entity.explosion.NapalmExplosion;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.ModExplosionHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class PerfumeBottle extends BrutalityAbstractPhysicsThrowingProjectile implements BrutalityGeoEntity {


    public PerfumeBottle(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, pLevel, damageTypeResourceKey);
    }

    public PerfumeBottle(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Player player, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, player, pLevel, damageTypeResourceKey);
    }

    @Override
    public int getInGroundLifespan() {
        return 200;
    }


    @Override
    public float getModelHeight() {
        return 8;
    }

    @Override
    public @NotNull SoundEvent getHitGroundSoundEvent() {
        return SoundEvents.SPLASH_POTION_BREAK;
    }


    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return SoundEvents.SPLASH_POTION_BREAK;
    }

    @Override
    protected float getBounciness() {
        return 0.0F;
    }

    @Override
    protected int getBounceQuota() {
        return 0;
    }

    @Override
    protected boolean shouldDiscardAfterBounce() {
        return true;
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        super.onHit(hitResult);
        discard();
        Vec3 loc = hitResult.getLocation();

        float radius = 2.5F;
        AABB aabb = getBoundingBox().inflate(radius);

        boolean isExplosive = level().getBlockStatesIfLoaded(aabb).anyMatch(block -> block.is(BlockTags.FIRE) || block.is(Blocks.LAVA));

        if (isExplosive) {
            NapalmExplosion explosion = new NapalmExplosion(level(), getOwner(), null, null, loc.x, loc.y, loc.z, 3, true,
                    ModUtils.getThrowingWeaponExplosionInteractionFromConfig());
            explosion.setEntityFilter(e -> e != getOwner());
            ModExplosionHelper.Server.explode(explosion, level(), true);
        } else {
            if (level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(BrutalityParticles.PERFUME_PARTICLE.get(), loc.x, loc.y, loc.z, 200, radius, radius, radius, 0);
            }

            LivingEntity target = level().getNearestEntity(LivingEntity.class, TargetingConditions.DEFAULT.selector(e -> e != getOwner()), null, loc.x, loc.y, loc.z, aabb);
            if (target != null) {
                level().getNearbyEntities(Mob.class, TargetingConditions.DEFAULT, target, target.getBoundingBox().inflate(radius)).forEach(e -> {
                    e.setAggressive(true);
                    e.setTarget(target);
                });
            }

        }
    }

}
