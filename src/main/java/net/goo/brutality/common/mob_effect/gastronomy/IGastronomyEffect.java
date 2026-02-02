package net.goo.brutality.common.mob_effect.gastronomy;

import net.goo.brutality.common.registry.BrutalityEffects;
import net.minecraft.world.entity.LivingEntity;

/**
 * Defines a combat-altering effect tied to the Gastronomy system.
 * <p>
 * Gastronomy effects are categorized into {@link Type#WET} and {@link Type#DRY} archetypes,
 * which interact with specific "Condition" effects like Mashed or Scored to determine
 * damage scaling and specialized combat triggers.
 * </p>
 */
public interface IGastronomyEffect {

    /**
     * Categorizes the effect to determine which environmental or status conditions
     * amplify its potency.
     */
    enum Type {
        /** Boosted by the {@link  BrutalityEffects#MASHED} condition. */
        WET,
        /** Boosted by the {@link BrutalityEffects#SCORED} condition. */
        DRY,
        /** Interacts with both Mashed and Scored conditions. */
        BOTH
    }

    /**
     * @return The {@link Type} of this effect, used for interaction checks.
     */
    Type getType();

    /**
     * Determines if the resulting multiplier should increase as the effect's
     * potency level (amplifier) increases.
     * * @return {@code true} if scaling should be applied; {@code false} to use base values only.
     */
    boolean scalesWithLevel();

    /**
     * Indicates if this effect should be included in the damage calculation pipeline.
     * * @return {@code true} if the effect modifies the final damage amount.
     */
    boolean modifiesDamage();

    /**
     * @return The starting multiplier for this effect at level 1 (e.g., 1.1F for +10%).
     */
    float baseMultiplier();

    /**
     * @return The additional multiplier value added per level beyond the first level.
     */
    float multiplierPerLevel();

    /**
     * Executes custom logic when damage occurs while this effect is active on the victim.
     * <p>
     * Use this for non-damage modifications, such as spawning particles, applying
     * sub-effects, or triggering sound cues.
     * </p>
     *
     * @param attacker The entity dealing the damage.
     * @param victim   The entity receiving the damage (possessing this effect).
     * @param level    The current amplifier level of the effect.
     */
    default void applyEffect(LivingEntity attacker, LivingEntity victim, int level) {}
}