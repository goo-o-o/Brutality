package net.goo.brutality.entity.base;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.registry.BrutalityDamageTypes;
import net.goo.brutality.util.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BrutalityAbstractThrowingProjectile extends BrutalityAbstractArrow implements BrutalityGeoEntity {
    protected boolean dealtDamage = false;
    protected Float damage;
    protected final ResourceKey<DamageType> damageTypeResourceKey;

    public BrutalityAbstractThrowingProjectile(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, pLevel);
        this.damageTypeResourceKey = damageTypeResourceKey;
    }

    public BrutalityAbstractThrowingProjectile(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Player player, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, player, pLevel);
        this.damageTypeResourceKey = damageTypeResourceKey;
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (damage == null) damage = ModUtils.getAttackDamage(getOwner());

    }

    protected float getDamageMultiplier() {
        return 1;
    }



    @Override
    public @NotNull SoundEvent getHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return SoundEvents.TRIDENT_HIT;
    }


    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.dealtDamage = pCompound.getBoolean("DealtDamage");
    }

    @Override
    protected boolean canHitEntity(@NotNull Entity entity) {
        return entity != getOwner() && !this.getClass().isInstance(entity);
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("DealtDamage", this.dealtDamage);

    }

    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }

    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        if (collideWithBlocks)
            super.onHitBlock(pResult);
    }

    protected void onHitEntity(EntityHitResult pResult) {
        Entity target = pResult.getEntity();
        Entity owner = getOwner();


        DamageSource damagesource = damageTypeResourceKey == BrutalityDamageTypes.THROWING_PIERCE ?
                BrutalityDamageTypes.throwing_pierce(owner, this) : BrutalityDamageTypes.throwing_blunt(owner, this);

        if (this.getPierceLevel() > 0) {
            this.setPierceLevel((byte) (this.getPierceLevel() - 1));
        } else dealtDamage = true;

        SoundEvent soundEvent = getHitEntitySoundEvent();


        if (target.hurt(damagesource, damage * getDamageMultiplier())) {
            if (target.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (target instanceof LivingEntity livingEntity && owner instanceof LivingEntity livingOwner) {
                EnchantmentHelper.doPostHurtEffects(livingEntity, livingOwner);
                EnchantmentHelper.doPostDamageEffects(livingOwner, target);
                this.doPostHurtEffects(livingEntity);
            }
        }
        float volume = 1.0F;

        this.playSound(soundEvent, volume, 1.0F);

    }

}