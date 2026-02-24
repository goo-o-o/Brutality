package net.goo.brutality.common.magic.spells.cosmic;

import net.goo.brutality.common.entity.spells.cosmic.CosmicCataclysmEntity;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.registry.BrutalityEntities;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.tooltip.SpellTooltipRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static net.goo.brutality.util.tooltip.SpellTooltipRenderer.SpellStatComponentType.RANGE;
import static net.goo.brutality.util.tooltip.SpellTooltipRenderer.SpellStatComponentType.SIZE;

public class CosmicCataclysmSpell extends BrutalitySpell {


    public CosmicCataclysmSpell() {
        super(MagicSchool.COSMIC,
                List.of(SpellCategory.CHANNELLING, SpellCategory.AOE),
                "cosmic_cataclysm",
                100, 10, 100, 20, 1, List.of(
                        new SpellTooltipRenderer.SpellStatComponent(RANGE, 15, 5, 0F, 100F),
                        new SpellTooltipRenderer.SpellStatComponent(SIZE, 3, 1, 3F, 50F)
                ));
    }

    @Override
    public float getDamageLevelScaling() {
        return 3;
    }

    @Override
    public float getManaCostLevelScaling() {
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
    public boolean onStartCast(Player player, ItemStack stack, int spellLevel) {
        BlockPos blockPos = ModUtils.getBlockLookingAt(player, false, getFinalStat(spellLevel, getStat(RANGE)));
        if (blockPos == null) return false;
        Vec3 targetPos = blockPos.getCenter();
        Vec3 spawnPos = targetPos.add(0, Math.min(50, spellLevel) * 10, 0);

        if (player.level() instanceof ServerLevel serverLevel) {
            CosmicCataclysmEntity spellEntity = new CosmicCataclysmEntity(BrutalityEntities.COSMIC_CATACLYSM_ENTITY.get(), serverLevel);
            spellEntity.setSpellLevel(spellLevel);
            spellEntity.setPos(spawnPos);
            spellEntity.setOwner(player);
            serverLevel.addFreshEntity(spellEntity);
        }
        return true;
    }
}
