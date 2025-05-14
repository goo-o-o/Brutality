package net.goo.armament.entity.custom.arrow;

import net.goo.armament.client.entity.ArmaGeoEntity;
import net.goo.armament.entity.base.ArmaArrow;
import net.goo.armament.registry.ModEntities;
import net.goo.armament.registry.ModParticles;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class LightArrow extends ArmaArrow implements ArmaGeoEntity {

    public LightArrow(EntityType<? extends ArmaArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public LightArrow(double pX, double pY, double pZ, Level pLevel) {
        super(ModEntities.LIGHT_ARROW.get(), pX, pY, pZ, pLevel);
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return new ItemStack(Items.ARROW);
    }

    @Override
    public String geoIdentifier() {
        return "light_arrow";
    }


    @Override
    public GeoAnimatable cacheItem() {
        return this;
    }

    AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public SimpleParticleType getFlightParticle() {
        return ModParticles.SPARKLE_PARTICLE.get();
    }

    @Override
    public double getGravity() {
        return 0.025F;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (pResult.getEntity() instanceof Mob mob && mob.getMobType().equals(MobType.UNDEAD)) {
            mob.invulnerableTime = 0;
            mob.hurt(mob.damageSources().playerAttack((Player) this.getOwner()), 10);
        }
    }
}
