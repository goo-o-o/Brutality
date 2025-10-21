package net.goo.brutality.entity.projectile.trident.physics_projectile;

import net.goo.brutality.entity.base.BrutalityAbstractPhysicsThrowingProjectile;
import net.goo.brutality.entity.base.BrutalityAbstractThrowingProjectile;
import net.goo.brutality.registry.BrutalityModBlocks;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Mug extends BrutalityAbstractPhysicsThrowingProjectile {
    private static final EntityDataAccessor<Integer> MUG_TYPE_INDEX = SynchedEntityData.defineId(Mug.class, EntityDataSerializers.INT);
    protected String[] types = new String[]{"", "_coffee", "_eeffoc"};
    private static final String MUG_TYPE_KEY = "mugType";

    public Mug(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, pLevel, damageTypeResourceKey);
        this.pickup = Pickup.ALLOWED;
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains(MUG_TYPE_KEY)) {
            this.setMugTypeIndex(pCompound.getInt(MUG_TYPE_KEY));
        }
    }

    public void setMugTypeIndex(int index) {
        this.entityData.set(MUG_TYPE_INDEX, index);
    }

    public int getMugIndex() {
        return this.entityData.get(MUG_TYPE_INDEX);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MUG_TYPE_INDEX, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt(MUG_TYPE_KEY, getMugIndex());
    }

    @Override
    public String texture() {
        return "mug" + types[getMugIndex()];
    }

    @Override
    public float getModelHeight() {
        return 6;
    }

    @Override
    public @NotNull SoundEvent getHitGroundSoundEvent() {
        return SoundEvents.GLASS_BREAK;
    }

    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return SoundEvents.GLASS_BREAK;
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        super.onHit(hitResult);
        if (level() instanceof ServerLevel serverLevel) {
            BlockState state = BrutalityModBlocks.MUG.get().defaultBlockState();
            Vec3 pos = hitResult.getLocation();
            serverLevel.sendParticles(
                    new BlockParticleOption(ParticleTypes.BLOCK, state).setPos(getOnPos()),
                    pos.x, pos.y, pos.z,
                    6,
                    0.05, 0.05, 0.05,
                    0.15
            );
        }
        discard();
    }

    @Override
    protected int getBounceQuota() {
        return 0;
    }
}
