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

import static net.goo.brutality.util.tooltip.SpellTooltipRenderer.SpellStatComponentType.PIERCE;

public class CrescentDart extends BrutalityShuriken implements BrutalityGeoEntity, IBrutalitySpellEntity {
    private static final EntityDataAccessor<Integer> SPELL_LEVEL_DATA = SynchedEntityData.defineId(CrescentDart.class, EntityDataSerializers.INT);
    // Track pierced entities to avoid hitting the same target twice in a row
    private final List<Integer> piercedEntityIds = Lists.newArrayList();
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public CrescentDart(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.collideWithBlocks = false;
    }

    @Override
    public void tick() {
        super.tick();

        BrutalitySpell spell = getSpell();
        int pierceCap = Mth.floor(spell.getFinalStat(getSpellLevel(), spell.getStat(PIERCE)));

        // Only home if we have remaining pierces
        if (this.piercedEntityIds.size() < pierceCap) {
            // Fix: Use correct X, Y, Z coordinates for searching
            LivingEntity target = this.level().getNearestEntity(
                    LivingEntity.class,
                    TargetingConditions.DEFAULT.selector(e -> !piercedEntityIds.contains(e.getId()) && e.isAlive() && e != getOwner()),
                    (LivingEntity) getOwner(),
                    this.getX(), this.getY(), this.getZ(),
                    this.getBoundingBox().inflate(8)
            );

            if (target != null) {
                // Smooth Homing Implementation
                Vec3 targetPos = target.position().add(0, target.getBbHeight() / 2.0, 0);
                Vec3 idealDir = targetPos.subtract(this.position()).normalize();

                double currentSpeed = getDeltaMovement().length();
                double homingStrength = 0.2; // Adjust for turn sharpness

                // Interpolate current motion toward target
                Vec3 newMovement = getDeltaMovement().add(idealDir.scale(homingStrength)).normalize().scale(currentSpeed);
                this.setDeltaMovement(newMovement);

                // Update rotations to match movement visually
                this.setYRot((float)(Mth.atan2(newMovement.x, newMovement.z) * (180F / Math.PI)));
                this.setXRot((float)(Mth.atan2(newMovement.y, newMovement.horizontalDistance()) * (180F / Math.PI)));
            } else if (!this.piercedEntityIds.isEmpty()) {
                // If we've hit something but no new targets are near, fizzle out
                this.discard();
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (pResult.getEntity() instanceof LivingEntity target) {
            if (piercedEntityIds.contains(target.getId())) return;

            piercedEntityIds.add(target.getId());
            float dmg = getFinalDamage(getSpell(), getOwner(), getSpellLevel());

            target.invulnerableTime = 0;
            target.hurt(getOwner() != null ? damageSources().indirectMagic(this, getOwner()) : damageSources().magic(), dmg);

            int pierceCap = Mth.floor(getSpell().getFinalStat(getSpellLevel(), getSpell().getStat(PIERCE)));
            if (piercedEntityIds.size() >= pierceCap) {
                this.discard();
            }
        }
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPELL_LEVEL_DATA, 1);
    }

    @Override
    public int getSpellLevel() { return this.entityData.get(SPELL_LEVEL_DATA); }

    @Override
    public float getSizeScaling() {
        return 0;
    }

    @Override
    public void setSpellLevel(int spellLevel) { this.entityData.set(SPELL_LEVEL_DATA, spellLevel); }

    @Override
    public BrutalitySpell getSpell() { return new CrescentDartSpell(); }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SpellLevel")) this.setSpellLevel(tag.getInt("SpellLevel"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SpellLevel", this.getSpellLevel());
    }

    @Override
    protected boolean tryPickup(@NotNull Player pPlayer) { return false; }

    @Override
    public boolean fireImmune() { return true; }

    @Override
    protected int getLifespan() { return 100; }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }
}