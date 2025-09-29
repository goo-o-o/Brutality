package net.goo.brutality.entity.spells.cosmic;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.entity.base.BrutalityAbstractArrow;
import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.magic.IBrutalitySpellEntity;
import net.goo.brutality.magic.spells.cosmic.CosmicCataclysmSpell;
import net.goo.brutality.network.ClientboundParticlePacket;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.particle.providers.WaveParticleData;
import net.goo.brutality.registry.BrutalityModParticles;
import net.goo.brutality.registry.BrutalityModSounds;
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
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.SpellStatComponents.SIZE;

public class CosmicCataclysmEntity extends BrutalityAbstractArrow implements BrutalityGeoEntity, IBrutalitySpellEntity {
    private static final EntityDataAccessor<Integer> SPELL_LEVEL_DATA = SynchedEntityData.defineId(CosmicCataclysmEntity.class, EntityDataSerializers.INT);
    protected boolean shouldApplyWaveEffect = true;

    public CosmicCataclysmEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected boolean tryPickup(Player pPlayer) {
        return false;
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return ItemStack.EMPTY;
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
        return new CosmicCataclysmSpell();
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
    public void tick() {
        super.tick();
        if (this.inGroundTime > 40) discard();
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        super.onHit(hitResult);
        int spellLevel = getSpellLevel();
        BrutalitySpell spell = getSpell();
        float size = spell.getFinalStat(spellLevel, spell.getStat(SIZE));
        WaveParticleData<?> waveParticleData = new WaveParticleData<>(BrutalityModParticles.COSMIC_WAVE.get(), size * 2F, 50);

        Vec3 loc = hitResult.getLocation();
        double x = loc.x(), y = loc.y(), z = loc.z();

        level().playLocalSound(this.getOnPos(), BrutalityModSounds.METEOR_CRASH.get(), SoundSource.AMBIENT, Math.min(spellLevel, 50),
                1.5F - (spellLevel / 50F), false);

        if (this.level() instanceof ServerLevel serverLevel && getOwner() instanceof LivingEntity owner) {
            PacketHandler.sendToNearbyClients(serverLevel, x, y, z, 128, new ClientboundParticlePacket(
                    waveParticleData, true, (float) x, (float) y + 0.1F, (float) z, 0, 0, 0,
                    0, 0, 0, 1
            ));

            if (shouldApplyWaveEffect)
                ModUtils.applyWaveEffect(serverLevel, this, Entity.class, waveParticleData, e -> e != owner,
                        e -> {
                            e.hurt(e.damageSources().flyIntoWall(), spell.getFinalDamage(owner, spellLevel));
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
                if (getOwner() != null)
                    entity.hurt(entity.damageSources().indirectMagic(getOwner(), null), this.getFinalDamage(spell, getOwner(), spellLevel));
                else
                    entity.hurt(entity.damageSources().magic(), this.getFinalDamage(spell, getOwner(), spellLevel));

            }

        }
    }


    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        this.setNoGravity(true);
    }

    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return SoundEvents.EMPTY;
    }

    @Override
    public SoundEvent getHitGroundSoundEvent() {
        return SoundEvents.EMPTY;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, 0, this::predicate));
    }

    private PlayState predicate(AnimationState<CosmicCataclysmEntity> state) {
        if (this.inGround) {
            state.setAndContinue(RawAnimation.begin().thenPlayAndHold("ground"));
        } else {
            state.setAndContinue(RawAnimation.begin().thenPlay("idle"));
        }
        return PlayState.CONTINUE;
    }

    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
