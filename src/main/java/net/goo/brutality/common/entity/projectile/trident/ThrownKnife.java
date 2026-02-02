package net.goo.brutality.common.entity.projectile.trident;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.common.entity.base.BrutalityAbstractTrident;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class ThrownKnife extends BrutalityAbstractTrident implements BrutalityGeoEntity {
    public String knifeType = "chef";
    private static final EntityDataAccessor<String> KNIFE_TYPE = SynchedEntityData.defineId(ThrownKnife.class, EntityDataSerializers.STRING);


    public ThrownKnife(Level pLevel, LivingEntity pShooter, ItemStack pStack, EntityType<? extends AbstractArrow> trident, String knifeType) {
        super(pLevel, pShooter, pStack, trident);
        setKnifeType(knifeType);
    }

    public ThrownKnife(EntityType<? extends BrutalityAbstractTrident> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public int getInGroundLifespan() {
        return 200;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(KNIFE_TYPE, "chef");
    }

    public void setKnifeType(String type) {
        this.entityData.set(KNIFE_TYPE, type);
        this.knifeType = type;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("KnifeType", getKnifeType());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setKnifeType(tag.getString("KnifeType"));
    }


    public String getKnifeType() {
        return this.entityData.get(KNIFE_TYPE);
    }

    @Override
    public String texture() {
        return "thrown_" + getKnifeType() + "_knife";
    }

    @Override
    protected Vec3 getTridentBounceStrength() {
        return new Vec3(-0.01D, -0.01D, -0.01D);
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return BrutalitySounds.KNIFE_BLOCK.get();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        pResult.getEntity().invulnerableTime = 0;
        super.onHitEntity(pResult);

    }
}
