package net.goo.brutality.entity.projectile.trident.physics_projectile;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.entity.base.BrutalityAbstractPhysicsThrowingProjectile;
import net.goo.brutality.entity.base.BrutalityAbstractThrowingProjectile;
import net.goo.brutality.network.ClientboundSyncCapabilitiesPacket;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.BrutalityModSounds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class StickyBomb extends BrutalityAbstractPhysicsThrowingProjectile implements BrutalityGeoEntity {


    public StickyBomb(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, pLevel, damageTypeResourceKey);
        this.damage = 0F;
    }

    public StickyBomb(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Player player, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, player, pLevel, damageTypeResourceKey);
        this.damage = 0F;
    }

    @Override
    public int getInGroundLifespan() {
        return 800;
    }


    @Override
    public float getModelHeight() {
        return 2;
    }

    @Override
    public @NotNull SoundEvent getHitGroundSoundEvent() {
        return BrutalityModSounds.TARGET_FOUND.get();
    }


    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return BrutalityModSounds.TARGET_FOUND.get();
    }

    @Override
    protected float getBounciness() {
        return 0.0F;
    }

    @Override
    protected int getBounceQuota() {
        return 0;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);

        Entity entity = pResult.getEntity();
        if (getOwner() instanceof Player owner) {
            entity.getCapability(BrutalityCapabilities.ENTITY_STICKY_BOMB_CAP).ifPresent(cap -> {
                cap.incrementStickyBombCount(owner.getUUID(), entity.getId());
                if (!level().isClientSide())
                    PacketHandler.sendToAllClients(new ClientboundSyncCapabilitiesPacket(entity.getId(), entity));
            });
            discard();
        }
    }
}
