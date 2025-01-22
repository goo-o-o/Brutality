package net.goo.armament.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class CruelSunEntity extends Entity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private UUID ownerUUID;

    public CruelSunEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState animationState) {
        animationState.getController().setAnimation(RawAnimation.begin().thenPlay("spawn").thenLoop("rotating"));
        return PlayState.CONTINUE;
    }

    public void setOwner(Player owner) {
        this.ownerUUID = owner.getUUID();
    }

    @Nullable
    public Player getOwner(Level level) {
        if (ownerUUID == null) {
            return null;
        }
        return level.getPlayerByUUID(ownerUUID);
    }

    @Override
    public void tick() {

        Player owner = getOwner(level());
        if (owner != null) {
            // Target position (floating above the player)
            double targetX = owner.getX();
            double targetY = owner.getY() + 5;
            double targetZ = owner.getZ();

            // Calculate the movement vector
            double deltaX = targetX - this.getX();
            double deltaY = targetY - this.getY();
            double deltaZ = targetZ - this.getZ();

            // Normalize the vector to get direction and scale by speed
            double speed = 0.2; // Adjust the speed as needed
            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

            if (distance > 0.01) { // Prevent division by zero for very small distances
                double moveX = (deltaX / distance) * speed;
                double moveY = (deltaY / distance) * speed;
                double moveZ = (deltaZ / distance) * speed;

                // Apply movement
                this.setDeltaMovement(moveX, moveY, moveZ);
            }

            // Move entity based on DeltaMovement
            this.move(MoverType.SELF, this.getDeltaMovement());
        }

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
    

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
    }

    @Override
    public boolean isColliding(BlockPos pPos, BlockState pState) {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false; // Prevents the fire animation from showing.
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void checkInsideBlocks() {
    }

    @Override
    public boolean canCollideWith(Entity pEntity) {
        return false;
    }
}
