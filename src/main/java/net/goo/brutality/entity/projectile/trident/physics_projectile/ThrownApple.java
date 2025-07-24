//package net.goo.brutality.entity.projectile.trident.physics_projectile;
//
//import net.goo.brutality.client.entity.BrutalityGeoEntity;
//import net.goo.brutality.entity.base.BrutalityAbstractPhysicsProjectile;
//import net.goo.brutality.entity.base.BrutalityAbstractTrident;
//import net.goo.brutality.registry.BrutalityModSounds;
//import net.minecraft.sounds.SoundEvent;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.projectile.AbstractArrow;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import org.jetbrains.annotations.NotNull;
//
//public class ThrownApple extends BrutalityAbstractPhysicsProjectile implements BrutalityGeoEntity {
//    public ThrownApple(Level pLevel, LivingEntity pShooter, ItemStack pStack, EntityType<? extends AbstractArrow> trident) {
//        super(pLevel, pShooter, pStack, trident);
//    }
//
//    public ThrownApple(EntityType<? extends BrutalityAbstractTrident> pEntityType, Level pLevel) {
//        super(pEntityType, pLevel);
//    }
//
//    @Override
//    public float getModelHeight() {
//        return 6;
//    }
//
//
//    @Override
//    protected @NotNull ItemStack getPickupItem() {
//        return ItemStack.EMPTY;
//    }
//
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
//
//
//    @Override
//    protected float getBounciness() {
//        return 0.25F;
//    }
//}
