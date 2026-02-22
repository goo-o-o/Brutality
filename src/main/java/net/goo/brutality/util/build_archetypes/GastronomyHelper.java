package net.goo.brutality.util.build_archetypes;

import net.goo.brutality.common.mob_effect.gastronomy.IGastronomyEffect;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.BrutalityTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

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

        float currentAmount = amount;

        // 1. Calculate the core multipliers for the Dry/Wet archetypes
        float scoredMultiplier = getGastronomyTypeMultiplier(victim, BrutalityEffects.SCORED.get());
        float mashedMultiplier = getGastronomyTypeMultiplier(victim, BrutalityEffects.MASHED.get());

        // 2. Check for global gastronomy-boosting Curios (Ice Cream Sandwich)
        boolean hasIceCreamBoost = CuriosApi.getCuriosInventory(attacker)
                .map(h -> h.isEquipped(BrutalityItems.ICE_CREAM_SANDWICH.get()))
                .orElse(false);

        // 3. Iterate through effects and process IGastronomyEffect implementations
        for (MobEffectInstance instance : victim.getActiveEffects()) {
            if (instance.getEffect() instanceof IGastronomyEffect gastro) {

                if (gastro.modifiesDamage()) {
                    float gastroScale = calculateGastroScale(gastro, instance);
                    float typeMult = getCombinedTypeMultiplier(gastro.getType(), scoredMultiplier, mashedMultiplier);

                    if (hasIceCreamBoost) {
                        typeMult += 0.2F;
                    }

                    currentAmount *= (1 + gastroScale * typeMult);
                }

                // Apply any secondary logic associated with the effect
                gastro.applyEffect(attacker, victim, instance.getAmplifier());
            }
        }

        return currentAmount;
    }

    /**
     * Calculates the baseline multiplier for a specific Gastronomy Type (Wet/Dry/Both).
     */
    private static float getCombinedTypeMultiplier(IGastronomyEffect.Type type, float scored, float mashed) {
        return switch (type) {
            case DRY -> scored;
            case WET -> mashed;
            case BOTH -> (scored + mashed) / 2F;
        };
    }

    /**
     * Determines the potency multiplier based on the amplifier of Scored or Mashed effects.
     */
    private static float getGastronomyTypeMultiplier(LivingEntity victim, net.minecraft.world.effect.MobEffect effect) {
        MobEffectInstance instance = victim.getEffect(effect);
        if (instance == null) return 1.05F; // Base 5% boost even without amplifier

        return 1.05F + (0.15F * instance.getAmplifier());
    }

    /**
     * Calculates the final scale of a gastronomy effect based on its level.
     */
    private static float calculateGastroScale(IGastronomyEffect gastro, MobEffectInstance instance) {
        float scale = gastro.baseMultiplier();
        if (gastro.scalesWithLevel()) {
            scale *= (1 + gastro.multiplierPerLevel() * (instance.getAmplifier() + 1));
        }
        return scale;
    }

    /**
     * Applies a status effect to an entity if a specific triggering item is equipped.
     * <p>
     * The duration of the effect is dynamically scaled based on the entity's
     * current refrigeration-related gear (e.g., a 2x or 3x multiplier).
     * </p>
     *
     * @param attacker  The entity attacking the victim.
     * @param victim    The entity receiving the effect.
     * @param effect    The {@link MobEffect} to be applied.
     * @param duration  The base duration (in ticks) before multipliers are applied.
     * @param amplifier The potency level of the effect (0 for Level I).
     */
    public static void applyEffect(LivingEntity attacker, Entity victim, MobEffect effect, int duration, int amplifier) {
        if (victim instanceof LivingEntity livingVictim) // For convenience’s sake from calling methods
            livingVictim.addEffect(new MobEffectInstance(effect, duration * getFridgeMult(attacker), amplifier));
    }

    /**
     * Calculates the duration multiplier based on the highest tier of refrigeration gear equipped.
     * <p>
     * <b>Multipliers:</b>
     * <ul>
     * <li>{@link BrutalityItems#SMART_FRIDGE}: 3x Duration</li>
     * <li>{@link BrutalityItems#FRIDGE}: 2x Duration</li>
     * <li>None: 1x Duration (Standard)</li>
     * </ul>
     * </p>
     *
     * @param attacker The {@link LivingEntity} to get the multiplier from
     * @return An integer multiplier for status effect durations.
     */
    private static int getFridgeMult(LivingEntity attacker) {
        return CuriosApi.getCuriosInventory(attacker).map(handler -> {
            if (handler.isEquipped(BrutalityItems.SMART_FRIDGE.get())) return 3;
            if (handler.isEquipped(BrutalityItems.FRIDGE.get())) return 2;
            return 1;
        }).orElse(1);
    }
}