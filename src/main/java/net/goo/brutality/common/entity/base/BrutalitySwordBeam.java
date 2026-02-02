package net.goo.brutality.common.entity.base;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BrutalitySwordBeam extends AbstractHurtingProjectile implements BrutalityGeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Integer> TARGETS_HIT = SynchedEntityData.defineId(BrutalitySwordBeam.class, EntityDataSerializers.INT);
    private boolean shouldNoClip = true;


    public BrutalitySwordBeam(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
        this.noCulling = true;
        this.setNoGravity(true);
    }

    // RENDER
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TARGETS_HIT, 0);
    }

    protected void setTargetsHit(int count) {
        this.entityData.set(TARGETS_HIT, count);
    }

    protected int getTargetsHit() {
        return this.entityData.get(TARGETS_HIT);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("targetsHit", getTargetsHit());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("targetsHit")) {
            this.setTargetsHit(getTargetsHit());
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }


    public int getLifespan() {
        return 60;
    }


    public void tick() {
        Entity entity = this.getOwner();
        if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {

            ///

            if (!this.hasBeenShot) {
                this.gameEvent(GameEvent.PROJECTILE_SHOOT, this.getOwner());
                this.hasBeenShot = true;
            }

            if (!this.leftOwner) {
                this.leftOwner = this.checkLeftOwner();
            }

            this.baseTick();

            ///

            if (this.shouldBurn()) {
                this.setSecondsOnFire(1);
            }

            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }

            this.checkInsideBlocks();
            Vec3 vec3 = this.getDeltaMovement();
            double d0 = this.getX() + vec3.x;
            double d1 = this.getY() + vec3.y;
            double d2 = this.getZ() + vec3.z;


            if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
                double horizontalDistance = vec3.horizontalDistance();
                this.setYRot((float) (Mth.atan2(vec3.x, vec3.z) * (double) (180F / (float) Math.PI)));
                this.setXRot((float) (Mth.atan2(vec3.y, horizontalDistance) * (double) (180F / (float) Math.PI)));
                this.yRotO = this.getYRot();
                this.xRotO = this.getXRot();
            }

            float f = this.getInertia();
            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    float f1 = 0.25F;
                    this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * f1, d1 - vec3.y * f1, d2 - vec3.z * f1, vec3.x, vec3.y, vec3.z);
                }

                f = 0.8F;
            }

            this.setDeltaMovement(vec3.add(this.xPower, this.yPower, this.zPower).scale(f));
            spawnTrailParticle();
            this.setPos(d0, d1, d2);
        } else {
            this.discard();
        }
        if (tickCount > getLifespan()) discard();
    }

    public float getDamage() {
        return 10F;
    }

    public int getPierceCap() {
        return 1;
    }

    public float getInertia() {
        return 0.925F;
    }

    public void spawnTrailParticle() {
    }

    @Override
    public float getPickRadius() {
        return 0;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }


    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        if (!this.shouldNoClip) {
            this.discard();
        }
    }

    public SimpleParticleType getHitParticle() {
        return null;
    }

    @Override
    protected void onHit(HitResult pResult) {
        Vec3 hitPos = pResult.getLocation();
        if (getHitParticle() != null && this.getOwner() instanceof ServerPlayer player && tickCount % 3 == 0) {
            player.serverLevel().sendParticles(getHitParticle(), hitPos.x, hitPos.y, hitPos.z, 1, 0, 0, 0, 0);
        }

        super.onHit(pResult);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (this.getOwner() != null) {
            Entity target = pResult.getEntity();
            if (pResult.getEntity() != this.getOwner() && !(pResult.getEntity() instanceof BrutalitySwordBeam)) {
                target.hurt(damageSources().playerAttack(((Player) this.getOwner())), getDamage());
                setTargetsHit(getTargetsHit() + 1);
                if (getPierceCap() == 0) return;
                if (getTargetsHit() >= getPierceCap()) {
                    this.discard();
                }
            }
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return super.canHitEntity(entity) && entity != this.getOwner() && !entity.noPhysics;
    }

    @Override
    public void setSilent(boolean pIsSilent) {
        super.setSilent(true);
    }

}
