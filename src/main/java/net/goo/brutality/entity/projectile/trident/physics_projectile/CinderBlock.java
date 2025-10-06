package net.goo.brutality.entity.projectile.trident.physics_projectile;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.config.BrutalityCommonConfig;
import net.goo.brutality.entity.base.BrutalityAbstractPhysicsTrident;
import net.goo.brutality.entity.base.BrutalityAbstractTrident;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class CinderBlock extends BrutalityAbstractPhysicsTrident implements BrutalityGeoEntity {

    public CinderBlock(EntityType<? extends BrutalityAbstractTrident> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.pickup = Pickup.DISALLOWED;
    }



    @Override
    public int getInGroundLifespan() {
        return 200;
    }



    @Override
    public float getModelHeight() {
        return 4;
    }

    @Override
    public @NotNull SoundEvent getHitGroundSoundEvent() {
        return SoundEvents.STONE_HIT;
    }


    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return SoundEvents.STONE_HIT;
    }

    @Override
    protected float getBounciness() {
        return 0.0F;
    }

    @Override
    protected int getBounceQuota() {
        return 0;
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        if (BrutalityCommonConfig.THROWING_WEAPONS_BREAK_BLOCKS.get()) return;
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level().getBlockState(pos);
        float destroySpeed = state.getDestroySpeed(level(), pos);
        if (destroySpeed <= 0.3F) {
            // Glass is 0.3
            level().destroyBlock(pos, true);
        }
    }

    @Override
    public float getGravity() {
        return 0.1F;
    }

    @Override
    protected Vec3 getTridentBounceStrength() {
        return super.getTridentBounceStrength().scale(2);
    }
}
