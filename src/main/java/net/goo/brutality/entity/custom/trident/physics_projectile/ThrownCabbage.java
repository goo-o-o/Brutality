package net.goo.brutality.entity.custom.trident.physics_projectile;

import net.goo.brutality.client.entity.ArmaGeoEntity;
import net.goo.brutality.entity.base.BrutalityAbstractPhysicsProjectile;
import net.goo.brutality.entity.base.BrutalityAbstractTrident;
import net.goo.brutality.registry.ModItems;
import net.goo.brutality.registry.ModParticles;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class ThrownCabbage extends BrutalityAbstractPhysicsProjectile implements ArmaGeoEntity {
    public ThrownCabbage(Level pLevel, LivingEntity pShooter, ItemStack pStack, EntityType<? extends AbstractArrow> trident) {
        super(pLevel, pShooter, pStack, trident);
    }

    public ThrownCabbage(EntityType<? extends BrutalityAbstractTrident> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public String geoIdentifier() {
        return "thrown_cabbage";
    }

    @Override
    public float getModelHeight() {
        return 8;
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ModItems.CABBAGE_TRIDENT.get());
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return level().getRandom().nextBoolean() ? SoundEvents.MOSS_FALL : SoundEvents.MOSS_HIT;
    }

    @Override
    protected SoundEvent getHitEntitySoundEvent() {
        return level().getRandom().nextBoolean() ? SoundEvents.MOSS_FALL : SoundEvents.MOSS_HIT;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        for (int i = 0; i < 16; i++) {
            this.level().addParticle(ModParticles.CABBAGE_PARTICLE.get(),
                    pResult.getLocation().x, pResult.getLocation().y + pResult.getEntity().getBbHeight() / 2, pResult.getLocation().z,
                    (random.nextDouble() - 0.5) * 0.3,
                    random.nextDouble() * 0.2,
                    (random.nextDouble() - 0.5) * 0.3);
        }
    }

    @Override
    protected float getBounciness() {
        return 0.25F;
    }
}
