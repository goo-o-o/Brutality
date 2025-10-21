package net.goo.brutality.entity;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.registry.BrutalityModBlocks;
import net.goo.brutality.registry.BrutalityModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ChairSeatEntity extends Entity implements BrutalityGeoEntity {
    private BlockPos chairPos;

    public ChairSeatEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.chairPos = BlockPos.ZERO;
        this.noPhysics = true;
        this.setNoGravity(true);
        this.setInvisible(true);
    }

    public ChairSeatEntity(Level level, BlockPos pos) {
        this(BrutalityModEntities.CHAIR_SEAT.get(), level);
        this.chairPos = pos.immutable();
        this.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5); // Sitting height
    }

    @Override
    protected void defineSynchedData() {
        // No synced data needed
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide && tickCount % 10 == 0) {
            BlockState state = level().getBlockState(chairPos);
            if (!(state.is(BrutalityModBlocks.WHITE_OFFICE_CHAIR.get()) || state.is(BrutalityModBlocks.BLACK_OFFICE_CHAIR.get()))) {
                this.discard();
            }

            if (this.getPassengers().isEmpty()) {
                this.discard();
            }

        }
    }

    @Override
    public void move(MoverType type, Vec3 pos) {
        // Prevent movement
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    public String texture() {
        return "empty";
    }

    @Override
    public String model() {
        return "empty";
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}