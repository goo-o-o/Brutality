package net.goo.brutality.magic.spells.brimwielder;

import net.goo.brutality.entity.spells.brimwielder.ExtinctionEntity;
import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.tooltip.BrutalityTooltipHelper;
import net.mcreator.terramity.init.TerramityModParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static net.goo.brutality.util.helpers.tooltip.BrutalityTooltipHelper.SpellStatComponents.*;

public class ExtinctionSpell extends BrutalitySpell {


    public ExtinctionSpell() {
        super(MagicSchool.BRIMWIELDER, List.of(SpellCategory.CONTINUOUS, SpellCategory.AOE), "extinction", 1, 1, 200, 0, 1, List.of(
                new BrutalityTooltipHelper.SpellStatComponent(RANGE, 25, 5, 25F, 100F),
                new BrutalityTooltipHelper.SpellStatComponent(PIERCE, 25, 5, 25F, 100F),
                new BrutalityTooltipHelper.SpellStatComponent(SIZE, 0.5F, 0.25F, 0.5F, 5F)
        ));
    }

    @Override
    public float getDamageLevelScaling() {
        return 0.5F;
    }

    @Override
    public float getManaCostLevelScaling() {
        return 0.25F;
    }

    @Override
    public int getCooldownLevelScaling() {
        return 5;
    }

    @Override
    public boolean onCastTick(Player player, ItemStack stack, int spellLevel) {
        if (getSpellEntity(player.level(), player) instanceof ExtinctionEntity extinctionEntity) {
            float range = getFinalStat(spellLevel, getStat(RANGE));
            float radius = getFinalStat(spellLevel, getStat(SIZE)) / 2;
            int pierce = (int) getFinalStat(spellLevel, getStat(PIERCE));

            if (player.level() instanceof ServerLevel serverLevel) {

                ModUtils.RayData<LivingEntity> rayData = ModUtils.getEntitiesInRay(LivingEntity.class, player, range,
                        ClipContext.Fluid.NONE, ClipContext.Block.OUTLINE, radius, e -> e != player, pierce,
                        null

                );

                extinctionEntity.setDataMaxLength(rayData.distance() - 2);

                if (rayData.entityList() == null) return false;

                for (LivingEntity target : rayData.entityList()) {
                    target.invulnerableTime = 0;
                    target.hurt(target.damageSources().indirectMagic(player, null), getFinalDamage(player, spellLevel));
                }

                Vec3 endPos = rayData.endPos();
                serverLevel.sendParticles(
                        TerramityModParticleTypes.STYGIAN_PARTICLE.get(), endPos.x, endPos.y, endPos.z, 5, 1, 1, 1, 0);

            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onStartCast(Player player, ItemStack stack, int spellLevel) {
        if (getSpellEntity(player.level(), player) == null) {
            ExtinctionEntity extinctionEntity = new ExtinctionEntity(BrutalityModEntities.EXTINCTION_ENTITY.get(), player.level());
            extinctionEntity.setOwner(player);
            extinctionEntity.setPos(player.getEyePosition().add(player.getLookAngle().scale(3.0)));
            extinctionEntity.setSpellLevel(spellLevel);
            player.level().addFreshEntity(extinctionEntity);
            return true;
        }
        return false;
    }

    @Override
    public void onEndCast(Player player, ItemStack stack, int spellLevel) {
        super.onEndCast(player, stack, spellLevel);
        if (getSpellEntity(player.level(), player) instanceof ExtinctionEntity extinctionEntity) {
            extinctionEntity.triggerAnim("controller", "despawn");
            DelayedTaskScheduler.queueServerWork(player.level(), 20, extinctionEntity::discard);
        }

    }

    private Entity getSpellEntity(Level level, Player player) {
        for (ExtinctionEntity entity : level.getEntitiesOfClass(ExtinctionEntity.class, player.getBoundingBox().inflate(25F), e -> e.getOwner() == player)) {
            return entity;
        }
        return null;
    }


}
