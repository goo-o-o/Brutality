package net.goo.armament.entity.custom;

import net.goo.armament.client.entity.ArmaGeoEntity;
import net.goo.armament.client.renderers.entity.ArmaGlowingEntityRenderer;
import net.goo.armament.registry.ModParticles;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SupernovaAsteroid extends ThrowableProjectile implements ArmaGeoEntity {
    private final float angleOffset;

    public SupernovaAsteroid(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel, float angleOffset) {
        super(pEntityType, pLevel);
        this.angleOffset = angleOffset;
    }


    @Override
    public String geoIdentifier() {
        return "supernova_asteroid";
    }

    @Override
    public GeoAnimatable cacheItem() {
        return null;
    }

    @Override
    protected void defineSynchedData() {

    }

    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this, true);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public <T extends Entity & ArmaGeoEntity, R extends GeoEntityRenderer<T>> void initGeo(Consumer<EntityRendererProvider<T>> consumer, Class<R> rendererClass) {
        ArmaGeoEntity.super.initGeo(consumer, ArmaGlowingEntityRenderer.class);
    }

    @Override
    protected void onHit(HitResult pResult) {
        Vec3 hitPos = pResult.getLocation();
        if (!level().isClientSide) {

            if (pResult.getType() == HitResult.Type.ENTITY) {
                Entity target = ((EntityHitResult) pResult).getEntity();

                if (target != this.getOwner() || target instanceof SupernovaAsteroid) {
                    target.hurt(target.damageSources().playerAttack((Player) this.getOwner()), 7.5F);
                    this.discard();

                    level().playSound(((Player) this.getOwner()), hitPos.x, hitPos.y, hitPos.z, SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 1F, 1F);
                    ((ServerLevel) level()).sendParticles(ModParticles.STARBURST_PARTICLE.get(), hitPos.x, hitPos.y + 0.5F, hitPos.z, 1, 0.5F, 0.5F, 0.5F, 0);

                    ((ServerPlayer) this.getOwner()).connection.send(new ClientboundSoundEntityPacket(ForgeRegistries.SOUND_EVENTS.getHolder(SoundEvents.GENERIC_EXPLODE).orElseThrow() , SoundSource.PLAYERS, this.getOwner(), 1F, 1F, 1));
                }
            }

            if (pResult.getType() == HitResult.Type.BLOCK) {
                ((ServerPlayer) Objects.requireNonNull(this.getOwner())).connection.send(new ClientboundSoundEntityPacket(ForgeRegistries.SOUND_EVENTS.getHolder(SoundEvents.GENERIC_EXPLODE).orElseThrow() , SoundSource.PLAYERS, this.getOwner(), 1F, 1F, 1));

                level().playSound(((Player) this.getOwner()), hitPos.x, hitPos.y, hitPos.z, SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 1F, 1F);
                ((ServerLevel) level()).sendParticles(ModParticles.STARBURST_PARTICLE.get(), hitPos.x, hitPos.y + 0.5F, hitPos.z, 1, 0.5F, 0.5F, 0.5F, 0);
                this.discard();
            }
        }

    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, (state) ->
                state.setAndContinue(RawAnimation.begin().thenLoop("falling")))
        );
    }

    @Override
    public void tick() {
        super.tick();
//        this.level().addParticle((new PlanetTrail.OrbData(0.6F, 0.15F, 0.6F, this.getId())), this.getX(), this.getY() + this.getBbHeight() / 2, this.getZ(), 0, 0, 0);


        if (getOwner() != null) {
            // Orbit the owner
            float spawnRadius = 3F;
            float speed = 1.25F;
            double plX = getOwner().getX() - (Mth.sin((float) tickCount / (8 / speed) + angleOffset) * spawnRadius);
            double plY = getOwner().getY() + (Mth.sin((float) tickCount / (4 / speed)) * 0.25);
            double plZ = getOwner().getZ() - (Mth.cos((float) tickCount / (8 / speed) + angleOffset) * spawnRadius);
            Vec3 plPos = new Vec3(plX, plY + 1F, plZ);
            this.setDeltaMovement(plPos.subtract(this.position()));

            // Handle interactions (e.g., explode on projectile collision)
            List<Projectile> projectiles = level().getEntitiesOfClass(Projectile.class, this.getBoundingBox())
                    .stream()
                    .filter(projectile -> projectile.getOwner() != getOwner())
                    .toList();
            if (!projectiles.isEmpty()) {
                for (Projectile projectile : projectiles) {
                    projectile.remove(RemovalReason.DISCARDED);
                    explode(); // Custom method to handle explosion
                }
            }
        } else {
            remove(RemovalReason.DISCARDED); // Remove if the owner is null
        }
    }

    public static Stream<SupernovaAsteroid> getAllAsteroidsOwnedBy(LivingEntity owner, ServerLevel level) {
        return StreamSupport.stream(level.getAllEntities().spliterator(), false)
                .filter(entity -> entity instanceof SupernovaAsteroid)
                .map(SupernovaAsteroid.class::cast)
                .filter(asteroid -> asteroid.getOwner() == owner);
    }

    public void explode() {
        // Handle explosion logic (e.g., damage entities, play effects)
        if (!level().isClientSide) {
            level().playSound(null, getX(), getY(), getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 1F, 1F);
            ((ServerLevel) level()).sendParticles(ModParticles.STARBURST_PARTICLE.get(), getX(), getY() + 0.5F, getZ(), 1, 0.5F, 0.5F, 0.5F, 0);
        }
        discard(); // Remove the asteroid after exploding
    }
}
