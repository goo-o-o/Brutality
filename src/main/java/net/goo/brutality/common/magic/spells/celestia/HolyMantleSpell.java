package net.goo.brutality.common.magic.spells.celestia;

import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.registry.BrutalitySpells;
import net.goo.brutality.util.tooltip.SpellTooltipRenderer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

import static net.goo.brutality.util.tooltip.SpellTooltipRenderer.SpellStatComponentType.DEFENSE;
import static net.goo.brutality.util.tooltip.SpellTooltipRenderer.SpellStatComponentType.DURATION;

public class HolyMantleSpell extends BrutalitySpell {


    public HolyMantleSpell() {
        super(MagicSchool.CELESTIA,
                List.of(SpellCategory.INSTANT, SpellCategory.SELF, SpellCategory.BUFF),
                "holy_mantle",
                50, 0, 200, 0, 1, List.of(
                        new SpellTooltipRenderer.SpellStatComponent(DURATION, 200, 40, null, null),
                        new SpellTooltipRenderer.SpellStatComponent(SpellTooltipRenderer.SpellStatComponentType.DEFENSE, 0, 4, null, null)
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
        player.addEffect(new MobEffectInstance(BrutalityEffects.GRACE.get(), (int) getFinalStat(spellLevel, getStat(DEFENSE)), spellLevel));
        return true;
    }

    public static void processHurt(LivingHurtEvent event, LivingEntity victim, float amount) {
        if (victim.hasEffect(BrutalityEffects.GRACE.get())) {
            MobEffectInstance effectInstance = victim.getEffect(BrutalityEffects.GRACE.get());
            if (effectInstance != null &&
                    amount <= effectInstance.getAmplifier() * BrutalitySpells.HOLY_MANTLE.get()
                            .getStat(SpellTooltipRenderer.SpellStatComponentType.DEFENSE).levelDelta()) {

                event.setCanceled(true);
                victim.removeEffect(BrutalityEffects.GRACE.get());
            }
        }
    }
}
