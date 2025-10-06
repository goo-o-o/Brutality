package net.goo.brutality.entity.spells.voidwalker;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.entity.base.BrutalityAbstractPhysicsProjectile;
import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.magic.IBrutalitySpellEntity;
import net.goo.brutality.magic.spells.voidwalker.GraviticImplosionSpell;
import net.goo.brutality.network.ClientboundParticlePacket;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.registry.BrutalityModSounds;
import net.mcreator.terramity.init.TerramityModParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class GraviticImplosionEntity extends BrutalityAbstractPhysicsProjectile implements BrutalityGeoEntity, IBrutalitySpellEntity {
    private static final EntityDataAccessor<Integer> SPELL_LEVEL_DATA = SynchedEntityData.defineId(GraviticImplosionEntity.class, EntityDataSerializers.INT);

    public GraviticImplosionEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
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
        return 16F / 32F;
    }

    @Override
    public float getModelHeight() {
        return 8;
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPELL_LEVEL_DATA, 1);
    }

    @Override
    protected int getLifespan() {
        return 100;
    }

    @Override
    public void setSpellLevel(int spellLevel) {
        this.entityData.set(SPELL_LEVEL_DATA, spellLevel);
    }

    @Override
    public BrutalitySpell getSpell() {
        return new GraviticImplosionSpell();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains(SPELL_LEVEL)) {
            this.setSpellLevel(tag.getInt(SPELL_LEVEL));
        }
    }

    @Override
    public @NotNull SoundEvent getHitGroundSoundEvent() {
        return SoundEvents.EMPTY;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt(SPELL_LEVEL, this.getSpellLevel());
    }

    @Override
    public float getGravity() {
        return 0.025F;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.inGround) {
            lockPitch();
            lockRoll();
            lockYaw();
        }

        if (this.inGroundTime > 20) discard();
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        triggerAnim("controller", "impact");
        int spellLevel = getSpellLevel();
        if (level() instanceof ServerLevel serverLevel) {

            DelayedTaskScheduler.queueServerWork(serverLevel, 6, () -> playSound(BrutalityModSounds.SPACE_EXPLOSION.get(), Math.min(spellLevel, 50),
                    1.5F - (spellLevel / 50F)));

            DelayedTaskScheduler.queueServerWork(serverLevel, 15, () -> {

                for (int i = 0; i < 16 + spellLevel * 4; i++) {
                    float randomX = (float) this.getRandomX(5 + spellLevel);
                    float randomY = (float) this.getY((random.nextDouble()) * (5 + spellLevel));
                    float randomZ = (float) this.getRandomZ(5 + spellLevel);

                    PacketHandler.sendToNearbyClients(serverLevel, this.getX(), this.getY(), this.getZ(), 128, new ClientboundParticlePacket(
                            TerramityModParticleTypes.ANTIMATTER.get(), true, randomX, randomY, randomZ, 0, 0, 0,
                            (float) (this.getX() - randomX) * (0.035F + 0.015F * (spellLevel + 1)),
                            (float) (this.getY() - randomY) * (0.035F + 0.015F * (spellLevel + 1)),
                            (float) (this.getZ() - randomZ) * (0.035F + 0.015F * (spellLevel + 1)), 1
                    ));
                }

                List<Entity> nearbyEntities = level().getEntities(this, this.getBoundingBox().inflate(spellLevel / 2F + 3),
                        e -> e instanceof LivingEntity && !(e instanceof Player && (e.isSpectator() || ((Player) e).isCreative())));

                for (Entity entity : nearbyEntities) {
                    if (entity != this && entity != getOwner()) {
                        Vec3 targetPos = entity.getPosition(1.0F);
                        Vec3 sourcePos = this.getPosition(1).add(0, this.getBbHeight(), 0);
                        Vec3 targetVector = sourcePos.subtract(targetPos).normalize();


                        entity.addDeltaMovement(targetVector.scale(0.15F).scale((1 + spellLevel) / 2F));
                        if (entity instanceof ServerPlayer player) {
                            player.connection.send(new ClientboundSetEntityMotionPacket(player));
                        }

                        float finalDamage = getSpell().getFinalDamage(getOwner(), spellLevel);
                        if (getOwner() != null)
                            entity.hurt(entity.damageSources().indirectMagic(getOwner(), null), finalDamage);
                        else
                            entity.hurt(entity.damageSources().magic(), finalDamage);
                    }

                }

            });

        }

    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        this.setNoGravity(true);
        this.setDeltaMovement(0,0,0);
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.EMPTY;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", state ->
                state.setAndContinue(RawAnimation.begin().thenLoop("idle")))
                .triggerableAnim("impact", RawAnimation.begin().thenPlayAndHold("impact")));
    }

    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected int getBounceCount() {
        return 0;
    }
}
