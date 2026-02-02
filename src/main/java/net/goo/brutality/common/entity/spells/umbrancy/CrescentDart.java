package net.goo.brutality.common.entity.spells.umbrancy;

import com.google.common.collect.Lists;
import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.common.entity.base.BrutalityShuriken;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.entity.spells.IBrutalitySpellEntity;
import net.goo.brutality.common.magic.spells.umbrancy.CrescentDartSpell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

import static net.goo.brutality.util.tooltip.BrutalityTooltipHelper.SpellStatComponents.PIERCE;

public class CrescentDart extends BrutalityShuriken implements BrutalityGeoEntity, IBrutalitySpellEntity {
    private static final EntityDataAccessor<Integer> SPELL_LEVEL_DATA = SynchedEntityData.defineId(CrescentDart.class, EntityDataSerializers.INT);
    private List<LivingEntity> piercedEntities = Lists.newArrayListWithCapacity(Mth.floor(getSpell().getFinalStat(getSpellLevel(), getSpell().getStat(PIERCE))));

    public CrescentDart(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.collideWithBlocks = false;
    }


    @Override
    protected boolean tryPickup(@NotNull Player pPlayer) {
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
        return new CrescentDartSpell();
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
        BrutalitySpell spell = getSpell();
        int pierceCap = Mth.floor(spell.getFinalStat(getSpellLevel(), spell.getStat(PIERCE)));
        int remainingCap = pierceCap - this.piercedEntities.size();

        if (remainingCap > 0) {
            LivingEntity nearest = level().getNearestEntity(LivingEntity.class, TargetingConditions.DEFAULT.selector(e -> !piercedEntities.contains(e)), null, getX(), getY(), getX(), this.getBoundingBox().inflate(15));
            if (nearest == null) {
                if (pierceCap != remainingCap)
                    discard();
            } else {
                Vec3 targetVec = nearest.getPosition(1).add(0, nearest.getBbHeight() / 2, 0).subtract(getPosition(1));
                targetVec.normalize();
                setDeltaMovement(targetVec.scale(0.35F));
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (pResult.getEntity() instanceof LivingEntity target) {
            if (piercedEntities.contains(target)) return;
            piercedEntities.add(target);
            float dmg = getFinalDamage(getSpell(), getOwner(), getSpellLevel());
            target.invulnerableTime = 0;
            target.hurt(getOwner() != null ? damageSources().indirectMagic(getOwner(), null) : damageSources().magic(), dmg);
        }
        int pierceCap = Mth.floor(getSpell().getFinalStat(getSpellLevel(), getSpell().getStat(PIERCE)));
        if ((piercedEntities.size() + 1) > pierceCap) discard();
    }

    @Override
    protected int getLifespan() {
        return 100;
    }


    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
