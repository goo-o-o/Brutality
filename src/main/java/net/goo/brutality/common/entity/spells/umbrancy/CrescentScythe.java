package net.goo.brutality.common.entity.spells.umbrancy;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.common.entity.base.BrutalityAbstractArrow;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.entity.spells.IBrutalitySpellEntity;
import net.goo.brutality.common.magic.spells.umbrancy.CrescentScytheSpell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CrescentScythe extends BrutalityAbstractArrow implements BrutalityGeoEntity, IBrutalitySpellEntity {
    private static final EntityDataAccessor<Integer> SPELL_LEVEL_DATA = SynchedEntityData.defineId(CrescentScythe.class, EntityDataSerializers.INT);

    public CrescentScythe(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.collideWithBlocks = false;
    }

    @Override
    protected boolean tryPickup(Player pPlayer) {
        return false;
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public float getSizeScaling() {
        return 0;
    }

    @Override
    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }

    @Override
    public int getSpellLevel() {
        return this.entityData.get(SPELL_LEVEL_DATA);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPELL_LEVEL_DATA, 1);
    }


    @Override
    public void setSpellLevel(int spellLevel) {
        this.entityData.set(SPELL_LEVEL_DATA, spellLevel);
    }

    @Override
    public BrutalitySpell getSpell() {
        return new CrescentScytheSpell();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains(SPELL_LEVEL)) {
            this.setSpellLevel(tag.getInt(SPELL_LEVEL));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt(SPELL_LEVEL, this.getSpellLevel());
    }


    @Override
    public void tick() {
        super.tick();
        if (getDeltaMovement().lengthSqr() < 64)
            this.addDeltaMovement(this.getDeltaMovement().scale(0.05));
    }

    @Override
    protected int getLifespan() {
        return 60;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();
        if (entity instanceof LivingEntity livingEntity && livingEntity != getOwner()) {
            float finalDamage = getFinalDamage(getSpell(), getOwner(), getSpellLevel()) * (level().isNight() ? 1.5F : 1);
            if (getOwner() != null)
                livingEntity.hurt(damageSources().indirectMagic(getOwner(), null), finalDamage);
            else
                livingEntity.hurt(damageSources().magic(), finalDamage);
        }
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.EMPTY;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, state ->
                state.setAndContinue(RawAnimation.begin().thenPlayAndHold("spawn"))));
    }


    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
