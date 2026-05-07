package net.goo.brutality.util.build_archetypes;

import net.goo.brutality.common.item.curios.BrutalityGastronomyCurioItem;
import net.goo.brutality.common.mob_effect.gastronomy.GastronomyEffect;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.util.BrutalityTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class for managing Gastronomy-related combat logic and status effects.
 * <p>
 * This helper handles the "Gastronomist" weapon archetype, where damage scales
 * based on specific debuffs (Scored/Mashed) and active Gastronomy-type effects
 * on the victim.
 * </p>
 */
public class GastronomyHelper {

    /**
     * Entry point for Gastronomist weapon damage calculations.
     *
     * @param attacker The player attacking.
     * @param victim   The entity being hit.
     * @param weapon   The item used for the attack.
     * @param amount   The current damage amount.
     * @return The new damage amount after gastronomy multipliers are applied.
     */
    public static float applyGastronomyDamageMultiplier(Player attacker, LivingEntity victim, ItemStack weapon, float amount) {
        if (!weapon.is(BrutalityTags.Items.GASTRONOMIST_ITEMS)) return amount;

        double wetBoost = victim.getAttributeValue(BrutalityAttributes.GASTRONOMY_WET_DEBUFF_DAMAGE_TAKEN_BOOST.get());
        double dryBoost = victim.getAttributeValue(BrutalityAttributes.GASTRONOMY_DRY_DEBUFF_DAMAGE_TAKEN_BOOST.get());
        double globalDealt = attacker.getAttributeValue(BrutalityAttributes.GASTRONOMY_DAMAGE_DEALT_BOOST.get());

        wetBoost *= getConditionMultiplier(victim, BrutalityEffects.MASHED.get());
        dryBoost *= getConditionMultiplier(victim, BrutalityEffects.SCORED.get());

        float totalMultiplier = (float) (1.0 + wetBoost + dryBoost + globalDealt);
        float finalAmount = amount * totalMultiplier;

        for (MobEffectInstance instance : victim.getActiveEffects()) {
            if (instance.getEffect() instanceof GastronomyEffect gastro) {
                gastro.applyEffect(attacker, victim, instance.getAmplifier());
            }
        }

        return finalAmount;
    }

    private static double getConditionMultiplier(LivingEntity entity, MobEffect condition) {
        MobEffectInstance effect = entity.getEffect(condition);
        if (effect == null) return 1.0;
        return 1.0 + (effect.getAmplifier() + 1) * 0.1;
    }
    
    public static void inflictGastronomyEffects(LivingEntity attacker, LivingEntity victim, ItemStack weapon) {
        boolean isGastronomistWeapon = weapon != null && weapon.is(BrutalityTags.Items.GASTRONOMIST_ITEMS);        // If it's a melee hit but NOT a Gastronomist weapon, we exit.

        CuriosApi.getCuriosInventory(attacker).ifPresent(handler -> {
            Set<BrutalityGastronomyCurioItem> uniqueGastronomyCurios = handler.findCurios(s -> s.getItem() instanceof BrutalityGastronomyCurioItem).stream().map(s -> ((BrutalityGastronomyCurioItem) s.stack().getItem())).collect(Collectors.toSet());

            Map<MobEffect, EffectData> effectMap = new HashMap<>();
            for (BrutalityGastronomyCurioItem item : uniqueGastronomyCurios) {
                // 1. Always process non-melee debuffs
                for (GastronomyDebuffContainer container : item.nonTrueMeleeDebuffs) {
                    updateEffectMap(effectMap, container);
                }

                // 2. Only process true melee debuffs if the weapon requirement is met
                if (isGastronomistWeapon) {
                    for (GastronomyDebuffContainer container : item.trueMeleeDebuffs) {
                        updateEffectMap(effectMap, container);
                    }
                }
            }
            int debuffBonus = Mth.ceil(attacker.getAttributeValue(BrutalityAttributes.GASTRONOMY_DEBUFF_LEVEL_MODIFIER.get()));
            double debuffDuration = attacker.getAttributeValue(BrutalityAttributes.GASTRONOMY_DEBUFF_DURATION_MULTIPLIER.get());
            effectMap.forEach((effect, data) ->
                    victim.addEffect(new MobEffectInstance(effect, (int) (data.maxDuration * debuffDuration), data.summedLevel - 1 + debuffBonus)));
        });
    }

    private static void updateEffectMap(Map<MobEffect, EffectData> map, GastronomyDebuffContainer container) {
        map.compute(container.effect().get(), (effect, data) -> {
            if (data == null) {
                return new EffectData(container.levels(), container.duration());
            }
            data.addLevel(container.levels());
            data.updateMaxDuration(container.duration());
            return data;
        });
    }

    private static class EffectData {
        int summedLevel;
        int maxDuration;

        EffectData(int level, int duration) {
            this.summedLevel = level;
            this.maxDuration = duration;
        }

        void addLevel(int level) {
            this.summedLevel += level;
        }

        void updateMaxDuration(int duration) {
            this.maxDuration = Math.max(this.maxDuration, duration);
        }
    }
}