package net.goo.brutality.entity.projectile.trident.physics_projectile;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.entity.base.BrutalityAbstractPhysicsThrowingProjectile;
import net.goo.brutality.entity.base.BrutalityAbstractThrowingProjectile;
import net.goo.brutality.entity.explosion.NapalmExplosion;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.ModExplosionHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Dynamite extends BrutalityAbstractPhysicsThrowingProjectile implements BrutalityGeoEntity {

    public Dynamite(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, pLevel, damageTypeResourceKey);
    }

    public Dynamite(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Player player, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, player, pLevel, damageTypeResourceKey);
    }

    @Override
    public int getInGroundLifespan() {
        return 200;
    }

    @Override
    public float getModelHeight() {
        return 2;
    }

    @Override
    public @NotNull SoundEvent getHitGroundSoundEvent() {
        return SoundEvents.CREEPER_PRIMED;
    }


    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return SoundEvents.CREEPER_PRIMED;
    }

    @Override
    protected float getBounciness() {
        return 0.25F;
    }

    @Override
    protected int getBounceQuota() {
        return 10;
    }

    @Override
    public void tick() {
        if (tickCount >= 60) {
            Vec3 loc = getPosition(1);
            explode(loc);
            discard();
        }
        super.tick();
    }

    private void explode(Vec3 loc) {
        NapalmExplosion explosion = new NapalmExplosion(level(), getOwner(), null, null, loc.x, loc.y, loc.z, 3, true,
                ModUtils.getThrowingWeaponExplosionInteractionFromConfig());
        explosion.setEntityFilter(e -> e != getOwner());
        ModExplosionHelper.Server.explode(explosion, level(), true);
    }
}
