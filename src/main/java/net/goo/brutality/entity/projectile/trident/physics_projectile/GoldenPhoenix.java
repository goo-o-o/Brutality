package net.goo.brutality.entity.projectile.trident.physics_projectile;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.entity.base.BrutalityAbstractPhysicsTrident;
import net.goo.brutality.entity.base.BrutalityAbstractTrident;
import net.goo.brutality.registry.BrutalityModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class GoldenPhoenix extends BrutalityAbstractPhysicsTrident implements BrutalityGeoEntity {

    public GoldenPhoenix(EntityType<? extends BrutalityAbstractTrident> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public GoldenPhoenix(EntityType<? extends BrutalityAbstractTrident> pEntityType, Player player, Level pLevel) {
        super(pEntityType, player, pLevel);
    }

    @Override
    public float getModelHeight() {
        return 16;
    }

    @Override
    protected int getLifespan() {
        return 200;
    }

    @Override
    @NotNull
    public SoundEvent getHitGroundSoundEvent() {
        return BrutalityModSounds.SQUELCH.get();
    }

    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return BrutalityModSounds.SQUELCH.get();
    }


    @Override
    protected int getBounceQuota() {
        return 1;
    }

    @Override
    protected float getBounciness() {
        return 0.25F;
    }
}
