package net.goo.armament.entity.base;

import net.goo.armament.client.entity.ArmaGeoEntity;
import net.goo.armament.particle.custom.SwordBeamTrail;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SwordBeam extends ThrowableProjectile implements ArmaGeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private int randomRoll;
    private int targetsHit = 0;

    public SwordBeam(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
        initializeRoll(level, getOrigin(), getBound());
        this.noCulling = true;
    }


    public int getOrigin() {
        return 0;
    }

    public int getBound() {
        return 361;
    }

    @Override
    public String geoIdentifier() {
        return "default";
    }

    @Override
    public GeoAnimatable cacheItem() {
        return null;
    }

    // RENDER
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
    }
    public float getRenderScale() {
        return 2F;
    }
    private void initializeRoll(Level level, int origin, int bound) {
        if (shouldInitializeRoll()) this.randomRoll = level.random.nextInt(origin, bound);
        else this.randomRoll = 0;
    }
    private boolean shouldInitializeRoll() { return true; }
    public int getRandomRoll() {
        return this.randomRoll;
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }


    // DATA
    public int getLifespan() {
        return 60;
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
        this.discard();
    }

    public SimpleParticleType getHitParticle() {
        return null;
    }

    @Override
    protected void onHit(HitResult pResult) {
        Vec3 hitPos = pResult.getLocation();
        if (getHitParticle() != null && this.getOwner() instanceof ServerPlayer player) {
//            player.connection.send(new ClientboundLevelParticlesPacket(
//                    getHitParticle(),
//                    true,
//                    hitPos.x, hitPos.y + getBbHeight() / 2, hitPos.z,
//                    0, 0, 0,
//                    0,
//                    10
//            ));
//
            player.serverLevel().sendParticles(getHitParticle(), hitPos.x, hitPos.y + 0.5, hitPos.z, 1,0,0,0,0);
        }

        super.onHit(pResult);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (this.getOwner() != null) {
            Entity target = pResult.getEntity();
            if (pResult.getEntity() != this.getOwner() && !(pResult.getEntity() instanceof SwordBeam)) {
                target.hurt(damageSources().playerAttack(((Player) this.getOwner())), getDamage());
                targetsHit++;
                if (getPierceCap() == 0) return;
                if (targetsHit >= getPierceCap()) {
                    this.discard();
                }
            }
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return super.canHitEntity(entity) && entity != this.getOwner();
    }

    @Override
    public void setSilent(boolean pIsSilent) {
        super.setSilent(true);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);

            if (hitresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }

            Vec3 motion = this.getDeltaMovement();

            float inertia = this.getInertia();
            this.checkInsideBlocks();
            double d0 = this.getX() + motion.x;
            double d1 = this.getY() + motion.y;
            double d2 = this.getZ() + motion.z;
            ProjectileUtil.rotateTowardsMovement(this, 0.2F);
            this.setDeltaMovement(motion.scale(inertia));

            this.level().addParticle((new SwordBeamTrail.OrbData(1F, 1F, 1F, random.nextFloat() * 0.3f, random.nextFloat() * 0.3f, this.getId(), getRandomRoll())), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            this.setPos(d0, d1, d2);

            if (tickCount >= getLifespan() || this.getDeltaMovement().length() < 0.1) {
                this.discard();
            }

        }

    }

}
