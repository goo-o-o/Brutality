package net.goo.armament.entity.custom;

import net.goo.armament.client.entity.ArmaGeoEntity;
import net.goo.armament.client.entity.ArmaGeoGlowingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class BlackHole extends ThrowableProjectile implements ArmaGeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private UUID ownerUUID;

    public BlackHole(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Override
    public CompoundTag getPersistentData() {
        return super.getPersistentData();
    }

    @Override
    protected void defineSynchedData() {
    }

    public void setOwner(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public Player getOwner(UUID ownerUUID) {
        return level().getPlayerByUUID(ownerUUID);
    }

    @Override
    public boolean canCollideWith(Entity pEntity) {
        return false;
    }

    @Override
    public void setNoGravity(boolean pNoGravity) {
        super.setNoGravity(true);
    }

    @Override
    public void tick() {
        Vec3 blackHolePos = this.getPosition(1.0F);
        super.tick();
        if (ownerUUID != null) {
            List<Entity> nearbyEntities = level().getEntities(this, this.getBoundingBox().inflate(7.5),
                    e -> e instanceof LivingEntity && !(e instanceof Player && (e.isSpectator() || ((Player) e).isCreative()) ));

            for (Entity entity : nearbyEntities) {
                if (entity != this && entity != level().getPlayerByUUID(ownerUUID)) {
                    Vec3 targetPos = entity.getPosition(1.0F);
                    Vec3 targetVector = blackHolePos.subtract(targetPos).normalize();

                    entity.addDeltaMovement(targetVector.scale(0.2F));
                    if (entity instanceof ServerPlayer player) {
                        player.connection.send(new ClientboundSetEntityMotionPacket(player));
                    }
                    entity.hurt(entity.damageSources().flyIntoWall(), 1);
                }
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    public String geoIdentifier() {
        return "black_hole";
    }

    @Override
    public GeoAnimatable cacheItem() {
        return null;
    }

    @Override
    public <T extends Entity & ArmaGeoEntity, R extends GeoEntityRenderer<T>> void initGeo(Consumer<EntityRendererProvider<T>> consumer, Class<R> rendererClass) {
        ArmaGeoEntity.super.initGeo(consumer, ArmaGeoGlowingEntityRenderer.class);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, (state) ->
                state.setAndContinue(RawAnimation.begin().thenLoop("idle")))
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}