package net.goo.brutality.common.magic.spells.cosmic;

import net.goo.brutality.common.entity.spells.cosmic.MeteorShowerEntity;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.registry.BrutalityEntities;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.tooltip.SpellTooltipRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static net.goo.brutality.common.magic.IBrutalitySpell.SpellCategory.*;
import static net.goo.brutality.util.tooltip.SpellTooltipRenderer.SpellStatComponentType.RANGE;
import static net.goo.brutality.util.tooltip.SpellTooltipRenderer.SpellStatComponentType.SIZE;

public class    MeteorShowerSpell extends BrutalitySpell {


    public MeteorShowerSpell() {
        super(MagicSchool.COSMIC,
                List.of(CONTINUOUS, AOE, TARGETABLE),
                "meteor_shower",
                10, 7, 100, 10, 1, List.of(
                        new SpellTooltipRenderer.SpellStatComponent(RANGE, 15, 5, 0F, 100F),
                        new SpellTooltipRenderer.SpellStatComponent(SIZE, 3, 0, 3F, 3F)
                ));
    }

    @Override
    public float getDamageLevelScaling() {
        return 0;
    }


    @Override
    public int getCooldownLevelScaling() {
        return 2;
    }

    @Override
    public int getCastTimeLevelScaling() {
        return -1;
    }

    @Override
    public boolean onCastTick(Player player, ItemStack stack, int spellLevel) {
        if (player.level() instanceof ServerLevel serverLevel) {
            BlockPos blockPos = ModUtils.getBlockLookingAt(player, false, getFinalStat(spellLevel, getStat(RANGE)));
            if (blockPos == null) return false;
            Vec3 targetPos = blockPos.getCenter();
            Vec3 spawnPos = targetPos.add(
                    Mth.nextFloat(serverLevel.random, -2, 2),
                    20,
                    Mth.nextFloat(serverLevel.random, -2, 2));

            MeteorShowerEntity spellEntity = new MeteorShowerEntity(BrutalityEntities.METEOR_SHOWER_ENTITY.get(), serverLevel);
            spellEntity.setSpellLevel(1);
            spellEntity.setPos(spawnPos);
            spellEntity.setOwner(player);
            spellEntity.addDeltaMovement(new Vec3(
                    Mth.nextFloat(serverLevel.random, -0.25F, 0.25F),
                    -0.5,
                    Mth.nextFloat(serverLevel.random, -0.25F, 0.25F)));
            serverLevel.addFreshEntity(spellEntity);
        }
        return true;
    }
}
