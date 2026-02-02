package net.goo.brutality.common.entity.projectile.generic;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.common.entity.base.BrutalityShuriken;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public class StarEntity extends BrutalityShuriken implements BrutalityGeoEntity {
    public boolean renderForLayer = false;

    public StarEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public StarEntity(@NotNull EntityType<? extends AbstractArrow> pEntityType, Level level, double x, double y, double z) {
        super(pEntityType, level, x, y, z);
    }


    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this, true);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    @Override
    public SoundEvent getHitGroundSoundEvent() {
        return BrutalitySounds.SHURIKEN_IMPACT.get();
    }

    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return BrutalitySounds.SHURIKEN_IMPACT.get();
    }

    @Override
    public void tick() {
        super.tick();

        if (life > 200 && !inGround) discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);

        Vec3 loc = pResult.getLocation();
        if (level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(BrutalityParticles.STAR_PARTICLE.get(),
                    loc.x, loc.y, loc.z, 10, 0.5, 0.5, 0.5, 0);
        }

        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        Vec3 loc = pResult.getLocation();

        if (level() instanceof ServerLevel serverLevel)
            serverLevel.sendParticles(BrutalityParticles.STAR_PARTICLE.get(),
                    loc.x, loc.y, loc.z, 10, 0.5, 0.5, 0.5, 0);

        super.onHitBlock(pResult);
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }


}
