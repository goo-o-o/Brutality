package net.goo.brutality.entity.projectile.trident.physics_projectile;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.entity.base.BrutalityAbstractPhysicsTrident;
import net.goo.brutality.entity.base.BrutalityAbstractTrident;
import net.goo.brutality.registry.BrutalityModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ThrownApple extends BrutalityAbstractPhysicsTrident implements BrutalityGeoEntity {


    public ThrownApple(EntityType<? extends BrutalityAbstractTrident> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ThrownApple(EntityType<? extends BrutalityAbstractTrident> pEntityType, Player player, Level pLevel) {
        super(pEntityType, player, pLevel);
    }


    @Override
    public float getModelHeight() {
        return 6;
    }


    @Override
    protected @NotNull ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }


    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return BrutalityModSounds.SQUELCH.get();
    }

    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return BrutalityModSounds.SQUELCH.get();
    }

    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected float getBounciness() {
        return 0.25F;
    }
}
