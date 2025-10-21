package net.goo.brutality.entity.projectile.generic;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.entity.base.BrutalityShuriken;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.BrutalityModParticles;
import net.goo.brutality.registry.BrutalityModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
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
        return BrutalityModSounds.SHURIKEN_IMPACT.get();
    }

    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return BrutalityModSounds.SHURIKEN_IMPACT.get();
    }

    @Override
    public void tick() {
        super.tick();

        if (life > 200 && !inGround) discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        Entity entity = pResult.getEntity();

        if (getOwner() instanceof Player player) {
            entity.getCapability(BrutalityCapabilities.ENTITY_STAR_COUNT_CAP).ifPresent(cap -> {
                cap.incrementStarCount(player.getUUID(), entity.getId());
            });
        }

        Vec3 loc = pResult.getLocation();
        if (level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(BrutalityModParticles.STAR_PARTICLE.get(),
                    loc.x, loc.y, loc.z, 10, 0.5, 0.5, 0.5, 0);
        }

        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        Vec3 loc = pResult.getLocation();

        if (level() instanceof ServerLevel serverLevel)
            serverLevel.sendParticles(BrutalityModParticles.STAR_PARTICLE.get(),
                    loc.x, loc.y, loc.z, 10, 0.5, 0.5, 0.5, 0);

        super.onHitBlock(pResult);
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }


}
