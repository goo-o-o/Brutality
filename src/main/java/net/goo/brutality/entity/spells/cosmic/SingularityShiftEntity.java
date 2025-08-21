package net.goo.brutality.entity.spells.cosmic;

import com.lowdragmc.photon.client.fx.EntityEffect;
import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.entity.base.BrutalityAbstractPhysicsProjectile;
import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.magic.IBrutalitySpellEntity;
import net.goo.brutality.magic.spells.cosmic.SingularityShiftSpell;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.registry.BrutalityModParticles;
import net.goo.brutality.util.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

import static net.goo.brutality.util.ModResources.GRAVITY_FIELD_DOWN_FX;
import static net.goo.brutality.util.ModResources.GRAVITY_FIELD_UP_FX;
import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.SpellStatComponents.DURATION;
import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.SpellStatComponents.SIZE;

public class SingularityShiftEntity extends BrutalityAbstractPhysicsProjectile implements BrutalityGeoEntity, IBrutalitySpellEntity {
    private static final EntityDataAccessor<Integer> SPELL_LEVEL_DATA = SynchedEntityData.defineId(SingularityShiftEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> WEIGHTLESS_DATA = SynchedEntityData.defineId(SingularityShiftEntity.class, EntityDataSerializers.BOOLEAN);

    public SingularityShiftEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public String texture() {
        return "star_stream";
    }

    @Override
    public String model() {
        return "star_stream";
    }

    @Override
    protected boolean tryPickup(Player pPlayer) {
        return false;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public float getSizeScaling() {
        return 0;
    }

    @Override
    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }

    @Override
    public int getSpellLevel() {
        return this.entityData.get(SPELL_LEVEL_DATA);
    }

    @Override
    public void setSpellLevel(int spellLevel) {
        this.entityData.set(SPELL_LEVEL_DATA, spellLevel);
    }

    public boolean isWeightless() {
        return this.entityData.get(WEIGHTLESS_DATA);
    }

    public void setWeightless(boolean weightless) {
        this.entityData.set(WEIGHTLESS_DATA, weightless);
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPELL_LEVEL_DATA, 1);
        this.entityData.define(WEIGHTLESS_DATA, true);
    }

    @Override
    public float getGravity() {
        return 0.01F;
    }

    @Override
    protected int getLifespan() {
        return 1500;
    }


    @Override
    public BrutalitySpell getSpell() {
        return new SingularityShiftSpell();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains(SPELL_LEVEL)) {
            this.setSpellLevel(tag.getInt(SPELL_LEVEL));
        }
        if (tag.contains("weightless")) {
            this.setWeightless(tag.getBoolean("weightless"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt(SPELL_LEVEL, this.getSpellLevel());
        tag.putBoolean("weightless", this.isWeightless());
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        super.onHit(hitResult);

        if (level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    BrutalityModParticles.COSMIC_PARTICLE.get(0).get(), this.getX(), this.getY(), this.getZ(),
                    2, 0.25, 0.25, 0.25, 0);
            serverLevel.sendParticles(
                    BrutalityModParticles.COSMIC_PARTICLE.get(1).get(), this.getX(), this.getY(), this.getZ(),
                    2, 0.25, 0.25, 0.25, 0);

            stopTriggeredAnimation(null, "show");
        } else {
                EntityEffect gravityField = new EntityEffect(isWeightless() ? GRAVITY_FIELD_UP_FX : GRAVITY_FIELD_DOWN_FX, this.level(), this, EntityEffect.AutoRotate.NONE);
                float scale = getSpell().getFinalStat(getSpellLevel(), getSpell().getStat(SIZE));
                scale *= 0.4F;
                scale -= 0.8F;
                gravityField.setScale(1 + scale, 1 + scale, 1 + scale);
                gravityField.start();
        }
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        triggerAnim(null, "show");

    }

    @Override
    public void tick() {
        super.tick();

        if (this.inGround && !this.level().isClientSide()) { // Ensure server-side
            if (tickCount % 5 == 0) {
                BrutalitySpell spell = getSpell();
                float range = spell.getFinalStat(getSpellLevel(), spell.getStat(SIZE));
                // Get entities in the cylinder
                List<LivingEntity> entities = ModUtils.getEntitiesInCylinder(
                        LivingEntity.class, this, e -> e != this, range + 0.5F, range * 2
                );

                // Apply effect to each entity
                entities.forEach(e -> {
                    // Create new MobEffectInstance for each entity
                    MobEffectInstance effectInstance = new MobEffectInstance(
                            isWeightless() ? BrutalityModMobEffects.WEIGHTLESSNESS.get() : BrutalityModMobEffects.HYPERGRAVITY.get(),
                            6,
                            getSpellLevel(),
                            false, true
                    );

                    e.addEffect(effectInstance);
                });
            }
        }
    }

    @Override
    protected int getInGroundLifespan() {
        return (int) getSpell().getFinalStat(getSpellLevel(), getSpell().getStat(DURATION));
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.EMPTY;
    }

    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, (state) ->
                state.setAndContinue(RawAnimation.begin().thenPlayAndHold("hide")))
                .triggerableAnim("show", RawAnimation.begin().thenPlayAndHold("show"))
        );
    }

    @Override
    protected int getBounceCount() {
        return 0;
    }
}
