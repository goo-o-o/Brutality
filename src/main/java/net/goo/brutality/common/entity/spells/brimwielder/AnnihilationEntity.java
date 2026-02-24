package net.goo.brutality.common.entity.spells.brimwielder;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.common.entity.base.BrutalityAbstractArrow;
import net.goo.brutality.common.entity.explosion.BloodExplosion;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.entity.spells.IBrutalitySpellEntity;
import net.goo.brutality.common.magic.spells.brimwielder.AnnihilationSpell;
import net.goo.brutality.util.ModExplosionHelper;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.mcreator.terramity.init.TerramityModParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
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
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Random;

import static net.goo.brutality.util.tooltip.SpellTooltipRenderer.SpellStatComponentType.CHANCE;

public class AnnihilationEntity extends BrutalityAbstractArrow implements BrutalityGeoEntity, IBrutalitySpellEntity {
    private static final EntityDataAccessor<Integer> SPELL_LEVEL_DATA = SynchedEntityData.defineId(AnnihilationEntity.class, EntityDataSerializers.INT);

    public AnnihilationEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Override
    protected boolean tryPickup(@NotNull Player pPlayer) {
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPELL_LEVEL_DATA, 1);
    }

    @Override
    public SoundEvent getHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }



    @Override
    public void setSpellLevel(int spellLevel) {
        this.entityData.set(SPELL_LEVEL_DATA, spellLevel);
    }

    @Override
    public BrutalitySpell getSpell() {
        return new AnnihilationSpell();
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
    public void onHitEntity(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() instanceof LivingEntity entity && entity != getOwner()) {
            if (!entity.level().isClientSide()) {
                entity.addEffect(new MobEffectInstance(TerramityModMobEffects.HEXED.get(), 60, 0));
            }
        }
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);

        Vec3 loc = pResult.getLocation();
        long seed = Double.doubleToLongBits(loc.x) ^ Double.doubleToLongBits(loc.y) ^ Double.doubleToLongBits(loc.z);

        Random seeded = new Random(seed);
        if (seeded.nextInt(0, 100) < getSpell().getFinalStat(getSpellLevel(), getSpell().getStat(CHANCE))) {
            BloodExplosion explosion = new BloodExplosion(level(), getOwner(), null, null, loc.x, loc.y, loc.z, 3, false, Level.ExplosionInteraction.NONE);
            explosion.damageScale = getSpellLevel() * 0.15F;
            explosion.setDamageFilter(e -> !(e instanceof AnnihilationEntity));
            ModExplosionHelper.Server.explode(explosion, level(), true);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide() && tickCount % 10 == 0 && !inGround) {
            level().addParticle(TerramityModParticleTypes.STYGIAN_PARTICLE.get(), this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F, 0.0F);
        }
        if (this.inGroundTime > 100) discard();
    }


    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return SoundEvents.TRIDENT_HIT;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }


    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
