package net.goo.brutality.entity.projectile.trident.physics_projectile;

import net.goo.brutality.entity.base.BrutalityAbstractPhysicsTrident;
import net.goo.brutality.entity.base.BrutalityAbstractTrident;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.registry.BrutalityModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ThrownStyrofoamCup extends BrutalityAbstractPhysicsTrident {

    public ThrownStyrofoamCup(EntityType<? extends BrutalityAbstractTrident> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ThrownStyrofoamCup(EntityType<? extends BrutalityAbstractTrident> pEntityType, Player player, Level pLevel) {
        super(pEntityType, player, pLevel);
    }

    @Override
    public float getModelHeight() {
        return 6;
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return BrutalityModSounds.STYROFOAM_IMPACT.get();
    }

    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return BrutalityModSounds.STYROFOAM_IMPACT.get();
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return new ItemStack(BrutalityModItems.STYROFOAM_CUP.get());
    }

    @Override
    protected int getBounceQuota() {
        return 0;
    }
}
