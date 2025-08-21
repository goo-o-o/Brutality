package net.goo.brutality.magic.spells.cosmic;

import net.goo.brutality.entity.spells.cosmic.MeteorShowerEntity;
import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static net.goo.brutality.magic.IBrutalitySpell.SpellCategory.*;
import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.SpellStatComponents.RANGE;
import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.SpellStatComponents.SIZE;

public class MeteorShowerSpell extends BrutalitySpell {


    public MeteorShowerSpell() {
        super(MagicSchool.COSMIC,
                List.of(CONTINUOUS, AOE, TARGET),
                "meteor_shower",
                10, 7, 100, 10, 1, List.of(
                        new BrutalityTooltipHelper.SpellStatComponent(RANGE, 15, 5, 0, 100),
                        new BrutalityTooltipHelper.SpellStatComponent(SIZE, 3, 0, 3, 3)
                ));
    }

    @Override
    public float getDamageLevelScaling() {
        return 0;
    }

    @Override
    public int getManaCostLevelScaling() {
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
    public boolean onCast(Player player, ItemStack stack, int spellLevel) {
        if (player.level() instanceof ServerLevel serverLevel) {
            BlockPos blockPos = ModUtils.getBlockLookingAt(player, false, getFinalStat(spellLevel, getStat(RANGE)));
            if (blockPos == null) return false;
            Vec3 targetPos = blockPos.getCenter();
            Vec3 spawnPos = targetPos.add(
                    Mth.nextFloat(serverLevel.random, -2, 2),
                    20,
                    Mth.nextFloat(serverLevel.random, -2, 2));

            MeteorShowerEntity spellEntity = new MeteorShowerEntity(BrutalityModEntities.METEOR_SHOWER_ENTITY.get(), serverLevel);
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
