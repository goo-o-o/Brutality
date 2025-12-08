package net.goo.brutality.entity.projectile.trident.physics_projectile;

import net.goo.brutality.config.BrutalityCommonConfig;
import net.goo.brutality.entity.base.BrutalityAbstractPhysicsThrowingProjectile;
import net.goo.brutality.entity.base.BrutalityAbstractThrowingProjectile;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class CinderBlock extends BrutalityAbstractPhysicsThrowingProjectile {


    public CinderBlock(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, pLevel, damageTypeResourceKey);
    }

    public CinderBlock(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Player player, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, player, pLevel, damageTypeResourceKey);
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
        if (destroySpeed <= 0.3F && state.getBlock().getExplosionResistance() < 1000) {
            // Glass is 0.3
            level().destroyBlock(pos, true);
        }
    }

    @Override
    public float getGravity() {
        return 0.1F;
    }

    @Override
    protected Vec3 getEntityBounceStrength() {
        return super.getEntityBounceStrength().scale(2);
    }
}
