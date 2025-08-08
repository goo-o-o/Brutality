package net.goo.brutality.magic.spells.voidwalker;

import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class IntangibleSpell extends BrutalitySpell {


    public IntangibleSpell() {
        super(MagicSchool.VOIDWALKER, SpellType.SINGLETON_SELF_BUFF, "intangible",
                50, 0, 200, 1, List.of(
                        new BrutalityTooltipHelper.SpellStatComponent(BrutalityTooltipHelper.SpellStatComponents.DURATION, 20, 10, null, null)
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
        BrutalityTooltipHelper.SpellStatComponent durationStat = getStat(BrutalityTooltipHelper.SpellStatComponents.DURATION);
        player.addEffect(new MobEffectInstance(TerramityModMobEffects.PHASING.get(), durationStat.base() + spellLevel * durationStat.levelDelta()));
        return true;
    }
}
