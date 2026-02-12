package net.goo.brutality.util.build_archetypes;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.config.BrutalityCommonConfig;
import net.goo.brutality.common.entity.capabilities.BrutalityCapabilities;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.ModTags;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

/**
 * Utility class for handling Rage-related mechanics and interactions in the game.
 * <p>
 * This includes applications of Rage gain, Rage value modifications, and triggering
 * critical Rage effects when thresholds are met.
 * </p>
 */
public class RageHelper {

    /**
     * Processes damage taken by a player and applies rage gain mechanics
     * if the required conditions are met, such as specific equipment or effects.
     *
     * @param player The player taking damage.
     * @param damage The amount of damage taken by the player before any modifications.
     */
    public static void processDamage(Player player, float damage) {
        // Prevent rage gain if the player is already in the Enraged state
        if (player.hasEffect(BrutalityEffects.ENRAGED.get())) return;

        // Ensure the player has the proper Curio equipment to enable rage gain
        Optional<ICuriosItemHandler> victimOpt = CuriosApi.getCuriosInventory(player).resolve();
        if (victimOpt.isPresent()) {
            ICuriosItemHandler handler = victimOpt.get();

            // Checks if any equipped Curio belongs to the 'rage_items' tag
            if (handler.isEquipped(BrutalityItems.ANGER_MANAGEMENT.get())) return;
            if (handler.isEquipped(item -> item.is(ModTags.Items.RAGE_ITEMS))) {

                // Apply Attribute scaling (e.g., items that increase rage gain efficiency)
                damage *= (float) player.getAttributeValue(BrutalityAttributes.DAMAGE_TO_RAGE_RATIO.get());

                // Apply global server configuration multiplier for balancing
                damage *= BrutalityCommonConfig.RAGE_GAIN_MULTIPLIER.get();
                modifyRageValue(player, damage);
                tryTriggerRage(player);
            }
        }
    }

    /**
     * Modifies the rage value of a player by a specified amount. If the player
     * is a server player, their updated rage value is synchronized with the client.
     *
     * @param player The player whose rage value is to be modified.
     * @param amount The amount by which to modify the player's rage value. Positive values increase
     *               rage, while negative values decrease it.
     */
    public static void modifyRageValue(Player player, float amount) {
        player.getCapability(BrutalityCapabilities.RAGE).ifPresent(cap -> {
            cap.modifyRageValue(amount);
            BrutalityCapabilities.sync(player, BrutalityCapabilities.RAGE);
        });
    }

    /**
     * Sets the rage value of the specified player. If the player is a server-side player,
     * the updated rage value is synchronized with the client.
     *
     * @param player The player whose rage value is to be set.
     * @param amount The new value for the player's rage. If the specified value is below zero,
     *               it will be clamped to zero.
     */
    public static void setRageValue(Player player, float amount) {
        player.getCapability(BrutalityCapabilities.RAGE).ifPresent(cap -> {
            cap.setRage(amount);
            BrutalityCapabilities.sync(player, BrutalityCapabilities.RAGE);
        });
    }

    /**
     * Triggers the rage effect on the specified player. This method calculates the player's rage level and duration
     * based on their current {@link BrutalityCapabilities#RAGE} capability and attributes. Upon activation, it applies
     * an {@link MobEffectInstance} of the {@link BrutalityEffects#ENRAGED} effect to the player.
     * The player's current rage value is reset after activation, and a sound effect is played in the world.
     *
     * @param player The {@link Player} entity whose rage effect is triggered. This player's rage capability and attributes
     *               are used to calculate the effect's level and duration. The player must have the {@code RAGE} capability
     *               for the method to execute properly.
     */
    public static void actuallyTriggerRage(Player player) {
        player.getCapability(BrutalityCapabilities.RAGE).ifPresent(cap -> {
            int rageLevel = (int) cap.getRage() / 100;
            float duration = (float) player.getAttributeValue(BrutalityAttributes.RAGE_TIME.get());
            rageLevel += (int) player.getAttributeValue(BrutalityAttributes.RAGE_LEVEL.get());

            player.addEffect(new MobEffectInstance(BrutalityEffects.ENRAGED.get(), (int) (duration * 20), rageLevel, false, true));
            RageHelper.setRageValue(player, 0);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 1F, 1F);
        });
    }

    /**
     * Attempts to trigger the rage effect for the specified {@link Player}. The method evaluates
     * the player's current rage value against their {@code MAX_RAGE} attribute to determine
     * if the rage effect should be activated. If the player's rage meets or exceeds the maximum rage value,
     * the method further checks if the player possesses specific equipment that suppresses rage triggering,
     * such as the {@link BrutalityItems#ANGER_MANAGEMENT}.
     * <p>
     * If the player is eligible and no suppressing item is equipped, the rage effect is triggered
     * through a call to {@link #actuallyTriggerRage(Player)}.
     *
     * @param player The {@link Player} whose rage status is being evaluated. This player must have the
     *               necessary capabilities and attributes for the method to operate properly. The method
     *               interacts with the {@link BrutalityCapabilities#RAGE} capability and retrieves the
     *               {@code MAX_RAGE} value from the player's attributes.
     */
    public static void tryTriggerRage(Player player) {
        int maxRage = (int) player.getAttributeValue(BrutalityAttributes.MAX_RAGE.get());

        player.getCapability(BrutalityCapabilities.RAGE).ifPresent(cap -> {
            if (cap.getRage() >= maxRage) {
                actuallyTriggerRage(player);
            }

        });
    }

    public static float getCurrentRagePercentage(Player player) {
        BrutalityCapabilities.sync(player, BrutalityCapabilities.RAGE);
        return player.getCapability(BrutalityCapabilities.RAGE).map(cap -> cap.getRage() / player.getAttributeValue(BrutalityAttributes.MAX_RAGE.get())).orElse(0D).floatValue();
    }

}