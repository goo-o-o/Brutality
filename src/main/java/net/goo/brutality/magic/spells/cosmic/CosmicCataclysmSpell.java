package net.goo.brutality.magic.spells.cosmic;

import net.goo.brutality.entity.spells.cosmic.CosmicCataclysmEntity;
import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.SpellStatComponents.RANGE;
import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.SpellStatComponents.SIZE;

public class CosmicCataclysmSpell extends BrutalitySpell {


    public CosmicCataclysmSpell() {
        super(MagicSchool.COSMIC,
                List.of(SpellCategory.CHANNELING, SpellCategory.AOE),
                "cosmic_cataclysm",
                100, 10, 100, 20, 1, List.of(
                        new BrutalityTooltipHelper.SpellStatComponent(RANGE, 15, 5, 0, 100),
                        new BrutalityTooltipHelper.SpellStatComponent(SIZE, 3, 1, 3, 50)
                ));
    }

    @Override
    public float getDamageLevelScaling() {
        return 3;
    }

    @Override
    public int getManaCostLevelScaling() {
        return 5;
    }

    @Override
    public int getCooldownLevelScaling() {
        return 2;
    }

    @Override
    public int getCastTimeLevelScaling() {
        return 3;
    }


    @Override
    public boolean onCast(Player player, ItemStack stack, int spellLevel) {
        if (player.level() instanceof ServerLevel serverLevel) {
            BlockPos blockPos = ModUtils.getBlockLookingAt(player, false, getFinalStat(spellLevel, getStat(RANGE)));
            if (blockPos == null) return false;
            Vec3 targetPos = blockPos.getCenter();
            Vec3 spawnPos = targetPos.add(0, Math.min(50, spellLevel) * 10, 0);

            CosmicCataclysmEntity spellEntity = new CosmicCataclysmEntity(BrutalityModEntities.COSMIC_CATACLYSM_ENTITY.get(), serverLevel);
            spellEntity.setSpellLevel(spellLevel);
            spellEntity.setPos(spawnPos);
            spellEntity.setOwner(player);
            serverLevel.addFreshEntity(spellEntity);
        }
        return true;
    }
}
