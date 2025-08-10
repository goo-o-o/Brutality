package net.goo.brutality.magic.spells.voidwalker;

import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.SpellStatComponents.DURATION;

public class IntangibleSpell extends BrutalitySpell {


    public IntangibleSpell() {
        super(MagicSchool.VOIDWALKER,
                List.of(SpellCategory.INSTANT, SpellCategory.SELF, SpellCategory.BUFF),
                "intangible",
                50, 0, 200, 0, 1, List.of(
                        new BrutalityTooltipHelper.SpellStatComponent(DURATION, 20, 10, null, null)
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
        return 15;
    }

    @Override
    public boolean onCast(Player player, ItemStack stack, int spellLevel) {
        player.addEffect(new MobEffectInstance(TerramityModMobEffects.PHASING.get(), (int) getFinalStat(spellLevel, getStat(DURATION))));
        return true;
    }
}
