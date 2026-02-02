package net.goo.brutality.util.attribute;

import net.goo.brutality.common.config.BrutalityCommonConfig;
import net.goo.brutality.common.item.base.BrutalityAnkletItem;
import net.goo.brutality.common.item.curios.necklace.FuzzyDice;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.goo.brutality.event.LivingDodgeEvent;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.item.ItemCategoryUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.ModLoader;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * The primary utility hub for processing Brutality-specific attributes.
 * <p>
 * This helper contains the mathematical logic for non-expansive attributes that do not
 * require a dedicated resource system (like Mana or Rage). It handles physics scaling,
 * combat recovery (Lifesteal/Omnivamp), and complex event-driven procs like Dodging and Crits.
 * </p>
 */
public class AttributeCalculationHelper {

    /**
     * Calculates modified jump power based on the {@link BrutalityAttributes#JUMP_HEIGHT} attribute.
     * <p>
     * <b>Calibration Logic:</b>
     * <ul>
     * <li>{@code 1.2522}: The baseline offset for zero additional height.</li>
     * <li>{@code 0.159}: The conversion factor where +0.159 motion delta equals +1 block height.</li>
     * </ul>
     * </p>
     *
     * @param livingEntity The jumping entity.
     * @param original     The base jump power (default 0.42f).
     * @return Adjusted jump power, or {@code null} if the attribute is missing.
     */
    @Nullable
    public static Float getJumpPowerFromAttribute(LivingEntity livingEntity, float original) {
        AttributeInstance jumpAttr = livingEntity.getAttribute(BrutalityAttributes.JUMP_HEIGHT.get());
        if (jumpAttr != null) {
            return (float) (original + ((jumpAttr.getValue() - 1.2522) * 0.159));
        }
        return null;
    }

    /**
     * Applies healing to the attacker based on Melee Damage dealt.
     */
    public static void handleLifesteal(float originalDamage, LivingEntity attacker) {
        attacker.heal((float) (originalDamage * attacker.getAttributeValue(BrutalityAttributes.LIFESTEAL.get())));
    }

    /**
     * Applies healing to the attacker based on ALL damage types dealt.
     */
    public static void handleOmnivamp(float originalDamage, LivingEntity attacker) {
        attacker.heal((float) (originalDamage * attacker.getAttributeValue(BrutalityAttributes.OMNIVAMP.get())));
    }

    /**
     * Scales incoming damage for a victim based on their Damage Taken attribute multiplier.
     *
     * @param originalDamage Initial damage amount.
     * @param victim         The entity taking damage.
     * @return The scaled damage amount.
     */
    public static float handleDamageTaken(float originalDamage, LivingEntity victim) {
        if (victim.getAttribute(BrutalityAttributes.DAMAGE_TAKEN.get()) != null) {
            return (float) (originalDamage * victim.getAttributeValue(BrutalityAttributes.DAMAGE_TAKEN.get()));
        }
        return originalDamage;
    }

    /**
     * Computes and modifies the damage value based on the player's attributes and the item stack being used.
     * The method evaluates the player's relevant attributes for the specified item type and applies their effects
     * to the original damage value.
     *
     * @param player         The player whose attributes are being considered, or {@code null} if no player is provided.
     * @param stack          The item stack being used, which determines the applicable attribute group.
     * @param originalDamage The initial base damage value before attribute modifications.
     * @return The modified damage value after applying the relevant player attributes.
     */
    public static double computeAttributes(@Nullable Player player, ItemStack stack, double originalDamage) {
        if (player == null) return originalDamage;

        Map<Predicate<ItemStack>, List<Attribute>> attributeMap = Map.of(
                ItemCategoryUtils::isSword, List.of(BrutalityAttributes.SWORD_DAMAGE.get(), BrutalityAttributes.SLASH_DAMAGE.get()),
                ItemCategoryUtils::isAxe, List.of(BrutalityAttributes.AXE_DAMAGE.get(), BrutalityAttributes.SLASH_DAMAGE.get()),
                ItemCategoryUtils::isHammer, List.of(BrutalityAttributes.HAMMER_DAMAGE.get(), BrutalityAttributes.BLUNT_DAMAGE.get()),
                ItemCategoryUtils::isScythe, List.of(BrutalityAttributes.SCYTHE_DAMAGE.get(), BrutalityAttributes.SLASH_DAMAGE.get()),
                ItemCategoryUtils::isSpearOrTrident, List.of(BrutalityAttributes.SPEAR_DAMAGE.get(), BrutalityAttributes.PIERCING_DAMAGE.get()),
                ItemCategoryUtils::isPickaxe, List.of(BrutalityAttributes.PIERCING_DAMAGE.get()),
                ItemCategoryUtils::isShovel, List.of(BrutalityAttributes.BLUNT_DAMAGE.get()),
                ItemCategoryUtils::isHoe, List.of(BrutalityAttributes.PIERCING_DAMAGE.get())
        );

        for (var entry : attributeMap.entrySet()) {
            if (entry.getKey().test(stack)) {
                for (Attribute attribute : entry.getValue()) {
                    AttributeInstance attributeInstance = player.getAttribute(attribute);
                    if (attributeInstance != null) {
                        originalDamage = calculateValue(attributeInstance, originalDamage);
                    }
                }
            }
        }
        return originalDamage;
    }

    /**
     * Calculates the final value of an attribute by applying its modifiers in sequential order.
     * The calculation considers three types of {@link AttributeModifier.Operation}: ADDITION,
     * MULTIPLY_BASE, and MULTIPLY_TOTAL, modifying the provided base value accordingly.
     *
     * @param attributeInstance The instance of the attribute whose modifiers are to be applied.
     * @param baseValue         The initial base value of the attribute before applying modifiers.
     * @return The calculated final value after applying all relevant modifiers.
     */
    private static double calculateValue(AttributeInstance attributeInstance, double baseValue) {

        for (AttributeModifier additionMultiplier : attributeInstance.getModifiersOrEmpty(AttributeModifier.Operation.ADDITION)) {
            baseValue += additionMultiplier.getAmount();
        }

        double finalValue = baseValue;

        for (AttributeModifier multiplyBaseModifier : attributeInstance.getModifiersOrEmpty(AttributeModifier.Operation.MULTIPLY_BASE)) {
            finalValue += baseValue * multiplyBaseModifier.getAmount();
        }

        for (AttributeModifier multiplyTotalModifier : attributeInstance.getModifiersOrEmpty(AttributeModifier.Operation.MULTIPLY_TOTAL)) {
            finalValue *= 1.0D + multiplyTotalModifier.getAmount();
        }

        return attributeInstance.getAttribute().sanitizeValue(finalValue);
    }

    /**
     * Inner utility for probability rolls influenced by the Luck effect/attribute.
     */
    public static class Luck {
        /**
         * Performs a probability roll.
         *
         * @param base      Base chance (0.0 to 1.0).
         * @param increment Extra chance per level of Luck.
         * @return {@code true} if the roll succeeds.
         */
        public static boolean roll(LivingEntity entity, float base, float increment) {
            float threshold = getChanceFromLuck(entity, base, increment);
            return entity.getRandom().nextFloat() < threshold;
        }

        /**
         * Calculates the total success threshold including Luck modifiers.
         */
        public static float getChanceFromLuck(LivingEntity entity, float base, float increment) {
            MobEffectInstance luck = entity.getEffect(MobEffects.LUCK);
            if (luck == null) return base;
            return base + (increment * (luck.getAmplifier() + 1));
        }
    }

    /**
     * Processes a potential stun proc when an attacker hits a victim.
     * <p>
     * Duration is modified by the victim's {@link BrutalityAttributes#TENACITY}.
     * </p>
     */
    public static void handleStunChance(LivingEntity attacker, LivingEntity victim) {
        AttributeInstance stunChanceAttr = attacker.getAttribute(BrutalityAttributes.STUN_CHANCE.get());
        AttributeInstance stunDurationAttr = attacker.getAttribute(BrutalityAttributes.STUN_DURATION.get());
        AttributeInstance tenacityAttr = victim.getAttribute(BrutalityAttributes.TENACITY.get());

        if (stunChanceAttr != null && stunDurationAttr != null && tenacityAttr != null) {
            float chance = attacker.getRandom().nextFloat();
            if (chance < stunChanceAttr.getValue()) {
                int finalStunDuration = (int) (stunDurationAttr.getValue() * (1 - tenacityAttr.getValue()));
                victim.addEffect(new MobEffectInstance(BrutalityEffects.STUNNED.get(), finalStunDuration));
            }
        }
    }

    /**
     * Scales movement friction (slowdown) based on Ground or Air friction attributes.
     * <p>
     * Uses Bedrock-style scaling where {@code Result = VanillaRate * AttributeValue}.
     * </p>
     *
     * @param livingEntity     The moving entity.
     * @param originalFriction The vanilla friction coefficient.
     * @return The new friction multiplier capped between 0.0 and 1.0.
     */
    public static float handleFriction(LivingEntity livingEntity, float originalFriction) {
        Attribute attributeToUse;
        double vanillaSlowdownRate;

        if (livingEntity.onGround()) {
            attributeToUse = BrutalityAttributes.GROUND_FRICTION.get();
            vanillaSlowdownRate = 1.0D - originalFriction;
        } else {
            attributeToUse = BrutalityAttributes.AIR_FRICTION.get();
            vanillaSlowdownRate = 0.09;
        }

        AttributeInstance frictionAttribute = livingEntity.getAttribute(attributeToUse);
        if (frictionAttribute == null) return originalFriction;

        double R_new = vanillaSlowdownRate * frictionAttribute.getValue();
        return (float) Mth.clamp(1.0D - R_new, 0.0F, 1.0F);
    }

    /**
     * Intercepts damage to perform a Dodge roll.
     * <p>
     * If successful, it cancels the damage, triggers the dodge sound, applies a short
     * cooldown, and triggers "On Dodge" logic for equipped Anklet curios.
     * </p>
     */
    public static void handleDodge(LivingEntity livingEntity, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.is(DamageTypeTags.BYPASSES_COOLDOWN)) return;
        if (livingEntity.hasEffect(BrutalityEffects.DODGE_COOLDOWN.get())) return;

        AttributeInstance dodgeAttr = livingEntity.getAttribute(BrutalityAttributes.DODGE_CHANCE.get());
        if (dodgeAttr != null && livingEntity.getRandom().nextFloat() < dodgeAttr.getValue()) {
            LivingDodgeEvent.Server dodgeEvent = new LivingDodgeEvent.Server(livingEntity, source, amount);
            ModLoader.get().postEvent(dodgeEvent);

            if (!dodgeEvent.isCanceled()) {
                if (livingEntity.level() instanceof ServerLevel serverLevel) {
                    serverLevel.playSound(null, livingEntity.getOnPos(), ModUtils.getRandomSound(BrutalitySounds.DODGE_SOUNDS), SoundSource.PLAYERS, 1F, Mth.nextFloat(livingEntity.getRandom(), 0.5F, 1.5F));
                }

                livingEntity.addEffect(new MobEffectInstance(BrutalityEffects.DODGE_COOLDOWN.get(), 10));

                // Trigger Anklet Curios
                CuriosApi.getCuriosInventory(livingEntity).ifPresent(itemHandler ->
                        itemHandler.getStacksHandler("anklet").ifPresent(stacksHandler -> {
                            for (int i = 0; i < stacksHandler.getSlots(); i++) {
                                ItemStack stack = stacksHandler.getStacks().getStackInSlot(i);
                                if (stack.getItem() instanceof BrutalityAnkletItem ankletItem) {
                                    ankletItem.onDodgeServer(livingEntity, source, amount, stack);
                                }
                            }
                        }));

                // Return false to signify the damage was dodged/canceled
                cir.setReturnValue(false);
            }
        }
    }

    /**
     * Modifies the distance at which mobs can see the player.
     */
    public static void handleEntityVisibility(LivingEvent.LivingVisibilityEvent event) {
        if (event.getEntity() instanceof Player player) {
            double stealth = player.getAttributeValue(BrutalityAttributes.STEALTH.get());
            event.modifyVisibility((1 - stealth)); // 0.2 would equal 80% of normal visibility
        }
    }

    /**
     * Processes critical hit logic, layering Vanilla crits with Brutality attribute crits.
     * <p>
     * If a Brutality crit occurs, it multiplies the existing modifier and potentially
     * triggers the {@link FuzzyDice} "Super Crit" roll.
     * </p>
     */
    public static void handleCrits(CriticalHitEvent event) {
        Player player = event.getEntity();

        boolean isVanillaCrit = event.isVanillaCritical();
        float critChanceAttr = (float) player.getAttributeValue(BrutalityAttributes.CRITICAL_STRIKE_CHANCE.get());
        float actualChance = Math.max(0, critChanceAttr);
        boolean isBrutalityCrit = player.getRandom().nextFloat() < actualChance;

        boolean allowVanilla = isVanillaCrit && BrutalityCommonConfig.VANILLA_CRITS.get();

        if (allowVanilla || isBrutalityCrit) {
            event.setResult(Event.Result.ALLOW);

            if (isBrutalityCrit) {
                float brutalityMod = (float) player.getAttributeValue(BrutalityAttributes.CRITICAL_STRIKE_DAMAGE.get());
                float finalModifier = event.getDamageModifier() * brutalityMod;
                event.setDamageModifier(finalModifier);

                // Check for Fuzzy Dice "Double Crit"
                FuzzyDice.handleDoubleCrit(player, event, actualChance);

                if (player.hasEffect(BrutalityEffects.PRECISION.get())) {
                    player.removeEffect(BrutalityEffects.PRECISION.get());
                }
            }
        } else {
            event.setResult(Event.Result.DENY);
        }
    }
}