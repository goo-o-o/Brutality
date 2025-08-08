package net.goo.brutality.entity.spells.cosmic;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.entity.base.BrutalityAbstractPhysicsProjectile;
import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.magic.IBrutalitySpellEntity;
import net.goo.brutality.particle.providers.WaveParticleData;
import net.goo.brutality.registry.BrutalityModParticles;
import net.goo.brutality.util.ModUtils;
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
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.SpellStatComponents.SIZE;

public class CosmicCataclysmSpellEntity extends BrutalityAbstractPhysicsProjectile implements BrutalityGeoEntity, IBrutalitySpellEntity {
    private static final EntityDataAccessor<Integer> SPELL_LEVEL_DATA = SynchedEntityData.defineId(CosmicCataclysmSpellEntity.class, EntityDataSerializers.INT);

    public CosmicCataclysmSpellEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
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
    public void setSpellLevel(int spellLevel) {
        this.entityData.set(SPELL_LEVEL_DATA, spellLevel);
    }

    @Override
    public BrutalitySpell getSpell() {
        return new net.goo.brutality.magic.spells.cosmic.CosmicCataclysmSpell();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains(SPELL_LEVEL)) {
            this.setSpellLevel(tag.getInt(SPELL_LEVEL));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt(SPELL_LEVEL, this.getSpellLevel());
    }

    @Override
    public float getGravity() {
        return 0.01F;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.inGround) {
            lockPitch();
            lockRoll();
            lockYaw();
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        int spellLevel = getSpellLevel();
        BrutalitySpell spell = getSpell();
        float size = spell.getFinalStat(spellLevel, spell.getStat(SIZE));
        WaveParticleData waveParticleData = new WaveParticleData(BrutalityModParticles.COSMIC_WAVE.get(), size, 50);

        if (this.level() instanceof ServerLevel serverLevel && getOwner() instanceof LivingEntity owner) {
            serverLevel.sendParticles(
                    waveParticleData,
                    owner.getX(),
                    owner.getY(),
                    owner.getZ(),
                    1,
                    0, 0, 0,
                    0
            );

            ModUtils.applyWaveEffect(serverLevel, owner, Entity.class, waveParticleData, e -> (e instanceof Projectile || (e instanceof LivingEntity && e != owner)),
                    e -> {
                        Vec3 ePos = e.getPosition(1);
                        Vec3 playerToEntity = owner.getPosition(1).vectorTo(ePos).normalize();
                        playerToEntity.add(0, 0.1, 0).scale(0.2 * spellLevel);

                        e.push(playerToEntity.x, playerToEntity.y, playerToEntity.z);
                        e.hurt(e.damageSources().flyIntoWall(), spell.getFinalDamage(owner, spell, spellLevel));
                        if (e instanceof Player) {
                            ((ServerPlayer) owner).connection.send(new ClientboundSetEntityMotionPacket(e));
                        }

                    });

        }

        List<Entity> nearbyEntities = level().getEntities(this, this.getBoundingBox().inflate(spellLevel / 2F + 3),
                e -> e instanceof LivingEntity && !(e instanceof Player && (e.isSpectator() || ((Player) e).isCreative())));

        for (Entity entity : nearbyEntities) {
            if (entity != this && entity != getOwner()) {
                Vec3 targetPos = entity.getPosition(1.0F);
                Vec3 sourcePos = this.getPosition(1).add(0, -2, 0);
                Vec3 targetVector = targetPos.subtract(sourcePos).normalize();


                entity.addDeltaMovement(targetVector.scale(0.2F).scale((1 + spellLevel)));
                if (entity instanceof ServerPlayer player) {
                    player.connection.send(new ClientboundSetEntityMotionPacket(player));
                }
                if (getOwner() instanceof Player owner)
                    entity.hurt(entity.damageSources().playerAttack(owner), getSpell().getFinalDamage(owner, getSpell(), spellLevel));
                else
                    entity.hurt(entity.damageSources().flyIntoWall(), getSpell().getFinalDamage(spellLevel));
            }

        }

        DelayedTaskScheduler.queueServerWork(40, this::discard);

    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        this.setNoGravity(true);
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.EMPTY;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
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
