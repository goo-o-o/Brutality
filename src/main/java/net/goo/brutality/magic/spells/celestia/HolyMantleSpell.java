package net.goo.brutality.magic.spells.celestia;

import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.util.helpers.tooltip.BrutalityTooltipHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static net.goo.brutality.util.helpers.tooltip.BrutalityTooltipHelper.SpellStatComponents.DURATION;

public class HolyMantleSpell extends BrutalitySpell {


    public HolyMantleSpell() {
        super(MagicSchool.CELESTIA,
                List.of(SpellCategory.INSTANT, SpellCategory.SELF, SpellCategory.BUFF),
                "holy_mantle",
                50, 0, 200, 0, 1, List.of(
                        new BrutalityTooltipHelper.SpellStatComponent(DURATION, 200, 40, null, null),
                        new BrutalityTooltipHelper.SpellStatComponent(BrutalityTooltipHelper.SpellStatComponents.DEFENSE, 0, 4, null, null)
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
        return 10;
    }

    @Override
    public boolean onStartCast(Player player, ItemStack stack, int spellLevel) {
        player.addEffect(new MobEffectInstance(BrutalityModMobEffects.GRACE.get(), (int) getFinalStat(spellLevel, getStat(DURATION)), spellLevel));
        return true;
    }
}
