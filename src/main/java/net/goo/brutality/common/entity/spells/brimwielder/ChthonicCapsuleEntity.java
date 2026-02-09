package net.goo.brutality.common.entity.spells.brimwielder;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.common.entity.base.BrutalityAbstractPhysicsProjectile;
import net.goo.brutality.common.entity.explosion.BloodExplosion;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.entity.spells.IBrutalitySpellEntity;
import net.goo.brutality.common.magic.spells.voidwalker.GraviticImplosionSpell;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.goo.brutality.util.ModExplosionHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import static net.goo.brutality.util.tooltip.SpellTooltips.SpellStatComponents.SIZE;

public class ChthonicCapsuleEntity extends BrutalityAbstractPhysicsProjectile implements BrutalityGeoEntity, IBrutalitySpellEntity {
    private static final EntityDataAccessor<Integer> SPELL_LEVEL_DATA = SynchedEntityData.defineId(ChthonicCapsuleEntity.class, EntityDataSerializers.INT);

    public ChthonicCapsuleEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
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
        return 16F / 8F;
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
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt(SPELL_LEVEL, this.getSpellLevel());
    }

    @Override
    public void tick() {
        super.tick();

        if (this.inGround) {
            lockPitch();
            lockRoll();
            lockYaw();
        }
        if (this.inGroundTime >= 100) {
            doExplosion();
            if (this.inGroundTime > 100)
                discard();
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        triggerAnim("controller", "impact");
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        doExplosion();
        discard();
    }

    private void doExplosion() {
        BloodExplosion explosion = new BloodExplosion(level(), getOwner(), null, null, this.getX(), this.getY(), this.getZ(), 3, false, Level.ExplosionInteraction.NONE);
        explosion.damage = getFinalDamage(getSpell(), getOwner(), getSpellLevel());

        explosion.setEntityFilter(e -> (!(e instanceof ChthonicCapsuleEntity)) && e != getOwner());
        ModExplosionHelper.Server.explode(explosion, level(), true);

        float size = getSpell().getFinalStat(getSpellLevel(), getSpell().getStat(SIZE));

        level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(size), e -> e != this).forEach(e -> {
            Vec3 pushAng = e.getPosition(1).subtract(this.getPosition(1));
            pushAng.normalize().scale(getSpellLevel() / 3F);
            e.push(pushAng.x(), pushAng.y(), pushAng.z());
            if (e instanceof ServerPlayer serverPlayer)
                serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer));
        });
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return BrutalitySounds.SQUELCH.get();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", state ->
                PlayState.CONTINUE)
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
