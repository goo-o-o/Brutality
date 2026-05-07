package net.goo.brutality.common.entity.projectile.trident;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.common.entity.base.BrutalityAbstractTrident;
import net.goo.brutality.util.lightning.ChainLightningHelper;
import net.mcreator.terramity.init.TerramityModParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
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


    public void tick() {
        if (level().isClientSide() && tickCount % level().getRandom().nextInt(5, 15) == 0) {
            ChainLightningHelper.Client.visualStaticArc(this, ChainLightningHelper.LightningType.THUNDERBOLT,
                    level().getRandom().nextInt(3, 8),
                    level().getRandom().nextFloat() * 5 + 3,
                    level().getRandom().nextFloat() * 0.2F + 0.05F,
                    level().getRandom().nextInt(3, 9));
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

        LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level());
        lightningBolt.setPos(pResult.getLocation());
        level().addFreshEntity(lightningBolt);
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);

        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(TerramityModParticleTypes.ELECTRIC_SHOCK_PARTICLE.get(), this.getX(), this.getY() + this.getBbHeight() / 2, this.getZ(),
                    10, 1, 1, 1, 0);
        }
    }

    AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}