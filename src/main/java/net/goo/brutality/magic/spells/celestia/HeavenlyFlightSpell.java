package net.goo.brutality.magic.spells.celestia;

import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class HeavenlyFlightSpell extends BrutalitySpell {


    public HeavenlyFlightSpell() {
        super(MagicSchool.CELESTIA, SpellType.SINGLETON_SELF_BUFF, "heavenly_flight",
                50, 0, 200, 1, List.of(
                        new BrutalityTooltipHelper.SpellStatComponent(BrutalityTooltipHelper.SpellStatComponents.DURATION, 200, 100, null, null)
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
        return 140;
    }

    @Override
    public boolean onCast(Player player, ItemStack stack, int spellLevel) {
        BrutalityTooltipHelper.SpellStatComponent durationStat = getStat(BrutalityTooltipHelper.SpellStatComponents.DURATION);
        player.addEffect(new MobEffectInstance(TerramityModMobEffects.MORTAL_FLIGHT.get(), durationStat.base() + spellLevel * durationStat.levelDelta()));
        player.playSound(BrutalityModSounds.WINGS_FLAP.get(), 1, Mth.nextFloat(player.getRandom(), 0.8F, 1.2F));
        return true;
    }
}
