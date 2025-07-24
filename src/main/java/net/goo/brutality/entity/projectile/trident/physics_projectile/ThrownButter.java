//package net.goo.brutality.entity.projectile.trident.physics_projectile;
//
//import net.goo.brutality.client.entity.BrutalityGeoEntity;
//import net.goo.brutality.entity.base.BrutalityAbstractPhysicsProjectile;
//import net.goo.brutality.entity.base.BrutalityAbstractTrident;
//import net.goo.brutality.registry.BrutalityModMobEffects;
//import net.goo.brutality.registry.BrutalityModSounds;
//import net.minecraft.sounds.SoundEvent;
//import net.minecraft.world.effect.MobEffectInstance;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.projectile.AbstractArrow;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.EntityHitResult;
//import org.jetbrains.annotations.NotNull;
//
//public class ThrownButter extends BrutalityAbstractPhysicsProjectile implements BrutalityGeoEntity {
//    public ThrownButter(Level pLevel, LivingEntity pShooter, ItemStack pStack, EntityType<? extends AbstractArrow> trident) {
//        super(pLevel, pShooter, pStack, trident);
//    }
//
//    public ThrownButter(EntityType<? extends BrutalityAbstractTrident> pEntityType, Level pLevel) {
//        super(pEntityType, pLevel);
//    }
//
//    @Override
//    public float getModelHeight() {
//        return 8;
//    }
//
//    @Override
//    protected @NotNull ItemStack getPickupItem() {
//        return ItemStack.EMPTY;
//    }
//
//    @Override
//    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
//        return BrutalityModSounds.SQUELCH.get();
//    }
//
//    @Override
//    public SoundEvent getHitEntitySound() {
//        return BrutalityModSounds.SQUELCH.get();
//    }
//
//    @Override
//    protected void onHitEntity(EntityHitResult pResult) {
//        super.onHitEntity(pResult);
//        if (pResult.getEntity() instanceof LivingEntity livingEntity) {
//            livingEntity.addEffect(new MobEffectInstance(BrutalityModMobEffects.STUNNED.get(), 3, 0));
//        }
//    }
//
//    @Override
//    protected float getBounciness() {
//        return 0.25F;
//    }
//}
