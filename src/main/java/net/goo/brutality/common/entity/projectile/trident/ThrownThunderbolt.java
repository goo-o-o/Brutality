package net.goo.brutality.common.entity.projectile.trident;

import com.lowdragmc.photon.client.fx.EntityEffect;
import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.common.entity.base.BrutalityAbstractTrident;
import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.goo.brutality.util.ClientModResources;
import net.mcreator.terramity.init.TerramityModParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import javax.annotation.Nullable;
import java.util.List;



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

    public float getDamage(@Nullable LivingEntity livingEntity) {
        return 8F;
    }

    public void tick() {
        if (firstTick && FMLEnvironment.dist == Dist.CLIENT && !(level() instanceof ServerLevel)) {
            EntityEffect lightningTrail = new EntityEffect(ClientModResources.getLightningTrailFX(), this.level(), this, EntityEffect.AutoRotate.NONE);
            lightningTrail.start();
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

        if (FMLEnvironment.dist == Dist.CLIENT && !(level() instanceof ServerLevel)) {
            EntityEffect lightningStrike = new EntityEffect(ClientModResources.getLightningStrikeBurstFX(), this.level(), this, EntityEffect.AutoRotate.NONE);
            lightningStrike.start();
        }

        if (level().isClientSide()) {
            this.level().setSkyFlashTime(2);
        } else {
            DelayedTaskScheduler.queueServerWork(level(), 3, () -> {
                List<Entity> hitEntities = this.level().getEntities(this, this.getBoundingBox().inflate(4), Entity::isAlive);

                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 10000.0F, 0.8F + this.random.nextFloat() * 0.2F);
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.WEATHER, 2.0F, 0.5F + this.random.nextFloat() * 0.2F);

                hitEntities.forEach(entity -> {
                    entity.invulnerableTime = 0;
                    entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
                    if (entity.getRemainingFireTicks() == 0) {
                        entity.setSecondsOnFire(8);
                    }
                    entity.hurt(this.damageSources().lightningBolt(), getDamage(getOwner() instanceof LivingEntity owner ? owner : null));
                });
            });
        }
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