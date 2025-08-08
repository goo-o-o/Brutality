package net.goo.brutality.magic.spells.celestia;

import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.mcreator.terramity.entity.HolyBeamEntity;
import net.mcreator.terramity.init.TerramityModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.SpellStatComponents.RANGE;

public class DivineRetributionSpell extends BrutalitySpell {


    public DivineRetributionSpell() {
        super(MagicSchool.CELESTIA, SpellType.SINGLETON_SELF_BUFF, "divine_retribution",
                50, 0, 20 * 40, 1, List.of(
                        new BrutalityTooltipHelper.SpellStatComponent(RANGE, 15, 5, null, null)
                ));
    }

    @Override
    public float getDamageLevelScaling() {
        return 0;
    }

    @Override
    public int getManaCostLevelScaling() {
        return 10;
    }

    @Override
    public int getCooldownLevelScaling() {
        return -20;
    }

    @Override
    public boolean onCast(Player player, ItemStack stack, int spellLevel) {
        BlockPos block = ModUtils.getBlockLookingAt(player, false, getFinalStat(spellLevel, getStat(RANGE)));
        if (block == null) return false;

        HolyBeamEntity holyBeam = new HolyBeamEntity(TerramityModEntities.HOLY_BEAM.get(), player.level());
        holyBeam.setPos(block.getCenter());
        player.level().addFreshEntity(holyBeam);
        return true;
    }
}
