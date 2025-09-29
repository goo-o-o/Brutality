package net.goo.brutality.entity.projectile.generic;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.entity.base.BrutalityAbstractArrow;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Locale;

public class FateCard extends BrutalityAbstractArrow implements BrutalityGeoEntity {
    private static final EntityDataAccessor<Integer> CARD_TYPE_INDEX = SynchedEntityData.defineId(FateCard.class, EntityDataSerializers.INT);

    public FateCard(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        setCardType(Mth.nextInt(RandomSource.create(), 0, CARD_TYPE.values().length - 1));

    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CARD_TYPE_INDEX, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("cardType", getCardIndex());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("cardType")) {
            this.setCardType(pCompound.getInt("cardType"));
        }
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    public CARD_TYPE getCardType() {
        return CARD_TYPE.values()[this.entityData.get(CARD_TYPE_INDEX)];
    }

    @Override
    public @NotNull SoundEvent getHitGroundSoundEvent() {
        return SoundEvents.VILLAGER_WORK_LIBRARIAN;
    }

    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return SoundEvents.VILLAGER_WORK_LIBRARIAN;
    }

    public int getCardIndex() {
        return this.entityData.get(CARD_TYPE_INDEX);
    }

    public void setCardType(int index) {
        this.entityData.set(CARD_TYPE_INDEX, index);
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount >= 100) discard();
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public enum CARD_TYPE {
        SLOW,
        STUN,
        IGNITE,
        POISON,
        WITHER
    }

    @Override
    public String texture() {
        return "fate_card_" + getCardType().name().toLowerCase(Locale.ROOT);
    }

    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this, true);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        Entity hitEntity = pResult.getEntity();
        if (getOwner() instanceof LivingEntity livingEntity) {
            float damage = (float) livingEntity.getAttributeValue(Attributes.ATTACK_DAMAGE);
            if (livingEntity instanceof Player player)
                hitEntity.hurt(damageSources().playerAttack(player), damage);
            else
                hitEntity.hurt(damageSources().mobAttack(livingEntity), damage);


        } else {
            hitEntity.hurt(damageSources().generic(), 5);
        }

        if (hitEntity instanceof LivingEntity livingHit) {
            switch (getCardType()) {
                case SLOW -> livingHit.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1));
                case STUN -> livingHit.addEffect(new MobEffectInstance(BrutalityModMobEffects.STUNNED.get(), 5, 0));
                case IGNITE -> livingHit.setSecondsOnFire(5);
                case POISON -> livingHit.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 1));
                case WITHER -> livingHit.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 1));
                default -> {
                }
            }

        }
        discard();
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }
}
