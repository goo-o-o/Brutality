package net.goo.brutality.common.entity.spells.brimwielder;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.entity.spells.IBrutalitySpellEntity;
import net.goo.brutality.common.magic.spells.brimwielder.AnnihilationSpell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DestructionEntity extends Entity implements BrutalityGeoEntity, IBrutalitySpellEntity {
    private static final EntityDataAccessor<Integer> SPELL_LEVEL_DATA = SynchedEntityData.defineId(DestructionEntity.class, EntityDataSerializers.INT);

    public DestructionEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public float getSizeScaling() {
        return 0.05F;
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
        this.entityData.define(SPELL_LEVEL_DATA, 1);
    }


    @Override
    public void setSpellLevel(int spellLevel) {
        this.entityData.set(SPELL_LEVEL_DATA, spellLevel);
    }

    @Override
    public BrutalitySpell getSpell() {
        return new AnnihilationSpell();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains(SPELL_LEVEL)) {
            this.setSpellLevel(tag.getInt(SPELL_LEVEL));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt(SPELL_LEVEL, this.getSpellLevel());
    }


    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > 20) discard();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, state ->
                state.setAndContinue(RawAnimation.begin().thenPlayAndHold("spawn"))));
    }

    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
