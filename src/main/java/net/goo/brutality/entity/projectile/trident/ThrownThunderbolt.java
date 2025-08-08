package net.goo.brutality.entity.projectile.trident;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.entity.base.BrutalityAbstractTrident;
import net.goo.brutality.particle.base.GenericTridentTrailParticle;
import net.mcreator.terramity.init.TerramityModParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class ThrownThunderbolt extends BrutalityAbstractTrident implements BrutalityGeoEntity {

    public ThrownThunderbolt(EntityType<? extends BrutalityAbstractTrident> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ThrownThunderbolt(Level pLevel, LivingEntity pShooter, ItemStack pStack, EntityType<? extends AbstractArrow> trident) {
        super(pLevel, pShooter, pStack, trident);
    }

    public float getGravity() {
        return 0.015F;
    }

    public float getDamage() {
        return 8F;
    }

    protected boolean summonsLightningByDefault() {
        return true;
    }

    private boolean trailSpawned = false;

    public void tick() {
        if (!trailSpawned && level().isClientSide) {
            this.level().addParticle((new GenericTridentTrailParticle.OrbData(0.8F, 0.8F, 0.0F, this.getBbHeight() * 0.75F, this.getId(), 0, 0, 0, "circle", 10)), this.getX(), this.getY() + getBbHeight() / 2, this.getZ(), 0, 0, 0);
            trailSpawned = true;
        }
        super.tick();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (getOwner() instanceof LivingEntity owner) {
            owner.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 3, false, false));
        }
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);

        if (this.level() instanceof ServerLevel serverLevel)
            serverLevel.sendParticles(TerramityModParticleTypes.ELECTRIC_SHOCK_PARTICLE.get(), this.getX() ,this.getY() + this.getBbHeight() / 2, this.getZ(),
                    10, 1, 1, 1, 0);

        this.discard();

    }

    AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}