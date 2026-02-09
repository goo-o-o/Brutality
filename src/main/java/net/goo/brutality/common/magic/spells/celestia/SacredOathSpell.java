package net.goo.brutality.common.magic.spells.celestia;

import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.util.tooltip.SpellTooltips;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static net.goo.brutality.util.tooltip.SpellTooltips.SpellStatComponents.DURATION;

public class SacredOathSpell extends BrutalitySpell {


    public SacredOathSpell() {
        super(MagicSchool.CELESTIA,
                List.of(SpellCategory.INSTANT, SpellCategory.SELF, SpellCategory.BUFF),
                "sacred_oath",
                50, 0, 200, 0, 1, List.of(
                        new SpellTooltips.SpellStatComponent(DURATION, 20, 10, null, null)
                ));
    }

    @Override
    public float getDamageLevelScaling() {
        return 0;
    }

    @Override
    public float getManaCostLevelScaling() {
        return 10;
    }

    @Override
    public int getCooldownLevelScaling() {
        return 15;
    }

    @Override
    public boolean onStartCast(Player player, ItemStack stack, int spellLevel) {
        player.addEffect(new MobEffectInstance(TerramityModMobEffects.PHASING.get(), (int) getFinalStat(spellLevel, getStat(DURATION))));
        return true;
    }
}
