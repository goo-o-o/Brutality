package net.goo.brutality.entity.projectile.trident.physics_projectile;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.entity.base.BrutalityAbstractPhysicsThrowingProjectile;
import net.goo.brutality.entity.base.BrutalityAbstractThrowingProjectile;
import net.goo.brutality.entity.explosion.NapalmExplosion;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.ModExplosionHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BlastBarrel extends BrutalityAbstractPhysicsThrowingProjectile implements BrutalityGeoEntity {


    public BlastBarrel(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, pLevel, damageTypeResourceKey);
    }

    public BlastBarrel(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Player player, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, player, pLevel, damageTypeResourceKey);
    }

    @Override
    public float getModelHeight() {
        return 16;
    }

    @Override
    public @NotNull SoundEvent getHitGroundSoundEvent() {
        return ModUtils.getRandomSound(BrutalityModSounds.CRATE_BREAK_SOUNDS);
    }

    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return ModUtils.getRandomSound(BrutalityModSounds.CRATE_BREAK_SOUNDS);
    }

    @Override
    protected float getBounciness() {
        return 0.5F;
    }

    @Override
    protected int getBounceQuota() {
        return 2;
    }

    @Override
    public float getGravity() {
        return 0.065F;
    }

    @Override
    public void tick() {
        if (tickCount >= 100) {
            Vec3 loc = getPosition(1);
            explode(loc);
            discard();
        }
        super.tick();
    }

    @Override
    protected void onFinalBounce(HitResult result) {
        Vec3 loc = result.getLocation();
        explode(loc);
    }

    @Override
    protected boolean shouldDiscardAfterBounce() {
        return true;
    }

    private void explode(Vec3 loc) {
        NapalmExplosion explosion = new NapalmExplosion(level(), getOwner(), null, null, loc.x, loc.y, loc.z, 3, true,
                ModUtils.getThrowingWeaponExplosionInteractionFromConfig());
        explosion.setEntityFilter(e -> e != getOwner());
        ModExplosionHelper.Server.explode(explosion, level(), true);
    }
}
