package net.goo.brutality.common.item.curios;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.BrutalityCategories;
import net.goo.brutality.common.item.base.BrutalityGenericItem;
import net.goo.brutality.util.attribute.AttributeContainer;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * A specialized Curio item base class that supports dynamic attribute modifiers and
 * custom animation/rendering behavior.
 */
public class BrutalityCurioItem extends BrutalityGenericItem implements ICurioItem {

    /**
     * A list of attribute templates used to generate modifiers when the item is equipped.
     * We use a template pattern to allow "building" items with different stats in the registry.
     */
    private List<AttributeContainer> attributeTemplates = List.of();
    public boolean followBodyRotations = true, followHeadRotations = true, translateIfSneaking = true, rotateIfSneaking = true;

    public BrutalityCurioItem(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }
    public BrutalityCurioItem(Rarity rarity) {
        super(rarity, List.of());
    }


    /**
     * Calculates a dynamic attribute bonus for a given entity based on the provided parameters.
     *
     * @param owner        The {@link LivingEntity} wearing the item that may provide a dynamic attribute bonus.
     *                     Typically the entity being evaluated for attribute modification.
     * @param stack        The {@link ItemStack} representing the gear equipped by the entity, which may modify the attribute.
     * @param attribute    The {@link Attribute} being evaluated for potential dynamic adjustment via this method.
     * @param currentBonus The current value of the attribute bonus before applying the dynamic computation.
     * @return A {@code double} value representing the additional or modified attribute bonus provided by this item.
     */
    public double getDynamicAttributeBonus(LivingEntity owner, ItemStack stack, Attribute attribute, double currentBonus) {
        return 0;
    }

    /**
     * Generates the attribute modifiers for this Curio when equipped.
     * <p>
     * To prevent UUID collisions between different attributes on the same item, this method
     * generates unique UUIDs by XORing the slot-provided UUID with the hash of the attribute's
     * description ID and the index of the template.
     * </p>
     *
     * @param slotContext Context regarding the Curio slot being queried.
     * @param uuid        The base UUID provided by the Curios API for this slot.
     * @param stack       The specific ItemStack being equipped.
     * @return A multimap of attributes and their respective modifiers.
     */
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (this.attributeTemplates.isEmpty()) {
            return ICurioItem.super.getAttributeModifiers(slotContext, uuid, stack);
        }

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        for (int i = 0; i < this.attributeTemplates.size(); i++) {
            AttributeContainer holder = this.attributeTemplates.get(i);

            // Deterministic unique UUID generation per attribute
            UUID attributeUUID = new UUID(
                    uuid.getMostSignificantBits() ^ holder.attribute().getDescriptionId().hashCode(),
                    uuid.getLeastSignificantBits() ^ i
            );
            AttributeModifier modifier = holder.createModifier(attributeUUID);

            builder.put(holder.attribute(), modifier);
        }

        return builder.build();
    }

    /**
     * Fluently assigns attribute templates to this item.
     * Typically called during registry initialization.
     *
     * @param attributes One or more {@link AttributeContainer} templates.
     * @return This item instance for chaining.
     */
    public BrutalityCurioItem withAttributes(AttributeContainer... attributes) {
        this.attributeTemplates = List.of(attributes);
        return this;
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.ItemType.CURIO;
    }


    public BrutalityCurioItem followBodyRotations(boolean shouldFollow) {
        this.followBodyRotations = shouldFollow;
        return this;
    }


    public BrutalityCurioItem followHeadRotations(boolean shouldFollow) {
        this.followHeadRotations = shouldFollow;
        return this;
    }

    public BrutalityCurioItem rotateIfSneaking(boolean shouldRotate) {
        this.rotateIfSneaking = shouldRotate;
        return this;
    }


    public BrutalityCurioItem translateIfSneaking(boolean shouldTranslate) {
        this.translateIfSneaking = shouldTranslate;
        return this;
    }

    /**
     * Triggers when the entity wearing this item heals.
     *
     * @param healed The entity being healed.
     * @param stack  The curio being worn.
     * @param amount The final amount of healing.
     */
    public float onWearerHeal(LivingEntity healed, ItemStack stack, float amount) {
        return amount;
    }


    /**
     * Triggers when the entity wearing this item takes damage.
     * <p>
     * This is useful for "Thorns" effects, damage reduction, or proccing
     * defensive buffs upon being struck.
     * </p>
     *
     * @param wearer The entity wearing the gear.
     * @param stack  The curio being worn.
     * @param source The source of the damage (e.g., arrow, explosion, or melee).
     * @param amount The final amount of damage being dealt to the wearer.
     */
    public float onWearerHurt(LivingEntity wearer, ItemStack stack, DamageSource source, float amount) {
        return amount;
    }

    /**
     * Triggers when the entity wearing this item deals damage to another entity.
     * <p>
     * Use this for offensive procs, such as lifesteal, applying debuffs to
     * the victim, or custom knockback logic.
     * </p>
     *
     * @param attacker The entity wearing the gear (the attacker).
     * @param stack    The curio being worn.
     * @param victim   The entity being struck by the attacker.
     * @param source   The source of the damage dealt.
     * @param amount   The final amount of damage dealt to the victim.
     */
    public float onWearerHit(LivingEntity attacker, ItemStack stack, Entity victim, DamageSource source, float amount) {
        return amount;
    }

    /**
     * Melee-sensitive version of {@link BrutalityCurioItem#onWearerHit}
     * Triggers when the entity wearing this item deals damage to another entity.
     * <p>
     * Use this for offensive procs, such as lifesteal, applying debuffs to
     * the victim, or custom knockback logic.
     * </p>
     *
     * @param attacker The entity wearing the gear (the attacker).
     * @param weapon   The stack used to attack the mob.
     * @param curio    The curio being worn.
     * @param victim   The entity being struck by the attacker.
     * @param source   The source of the damage dealt.
     * @param amount   The final amount of damage dealt to the victim.
     */
    public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, DamageSource source, float amount) {
        return amount;
    }

    /**
     * Triggers when the entity wearing this item deals kills another entity.
     * <p>
     * Use this for offensive procs, such as lifesteal, applying debuffs to
     * the victim, or custom knockback logic.
     * </p>
     *
     * @param attacker The entity wearing the gear (the attacker).
     * @param stack    The curio being worn.
     * @param victim   The entity being struck by the attacker.
     * @param source   The source of the damage dealt.
     */
    public void onWearerKill(LivingEntity attacker, ItemStack stack, Entity victim, DamageSource source) {

    }

    /**
     * Triggers when the entity wearing this item falls
     * Unlike others, this is a void method, edit the event directly, this allows you to cancel the event if needed
     *
     * @param event The event containing the required information
     */
    public void onWearerFall(LivingFallEvent event, ItemStack curio) {
    }

    /**
     * This is where the relevant methods (e.g. {@link BrutalityCurioItem#onWearerKill(LivingEntity, ItemStack, Entity, DamageSource)} will be called from
     */
    public static class Hooks {

        /**
         * Applies healing effects for curios worn by the given entity. This method is used to modify
         * the provided heal amount based on the behavior implemented by each {@link BrutalityCurioItem}
         * equipped by the wearer.
         *
         * @param healed The {@link LivingEntity} being healed. This is the entity wearing the curios.
         * @param amount The original amount of healing being applied to the entity.
         * @return The modified healing amount after applying the effects of all applicable curios.
         */
        public static float applyOnWearerHeal(LivingEntity healed, float amount) {
            float current = amount;
            Optional<ICuriosItemHandler> opt = CuriosApi.getCuriosInventory(healed).resolve();
            if (opt.isPresent()) {
                for (SlotResult result : opt.get().findCurios(s -> s.getItem() instanceof BrutalityCurioItem)) {
                    current = ((BrutalityCurioItem) result.stack().getItem()).onWearerHeal(healed, result.stack(), current);
                }
            }
            return current;
        }

        /**
         * Applies the effects of any equipped {@link BrutalityCurioItem} when the wearer
         * experiences a fall. This method is invoked during a {@link LivingFallEvent}.
         *
         * @param event The {@link LivingFallEvent} representing the fall event that
         *              occurred for the entity wearing the item.
         */
        public static void applyOnWearerFall(LivingFallEvent event) {
            Optional<ICuriosItemHandler> opt = CuriosApi.getCuriosInventory(event.getEntity()).resolve();
            if (opt.isPresent()) {
                for (SlotResult result : opt.get().findCurios(s -> s.getItem() instanceof BrutalityCurioItem)) {
                    ((BrutalityCurioItem) result.stack().getItem()).onWearerFall(event, result.stack());
                }
            }
        }

        /**
         * Applies specific effects or behavior when the wearer of a {@link BrutalityCurioItem}
         * kills another entity. This method checks the killer's inventory for any equipped
         * {@link BrutalityCurioItem}s and triggers their corresponding {@code onWearerKill} method.
         *
         * @param killer       The entity that performed the kill. This is typically the wearer
         *                     of the curio.
         * @param victim       The entity that was killed by the killer.
         * @param damageSource The source of damage that caused the kill. This provides context
         *                     about how the victim was killed.
         */
        public static void applyOnWearerKill(LivingEntity killer, LivingEntity victim, DamageSource damageSource) {
            Optional<ICuriosItemHandler> opt = CuriosApi.getCuriosInventory(killer).resolve();
            if (opt.isPresent()) {
                for (SlotResult result : opt.get().findCurios(s -> s.getItem() instanceof BrutalityCurioItem)) {
                    ((BrutalityCurioItem) result.stack().getItem()).onWearerKill(killer, result.stack(), victim, damageSource);
                }
            }
        }

        /**
         * Calls the {@code onWearerMeleeHit} method for each equipped curio of type {@link BrutalityCurioItem}
         * when the wearer attacks an enemy with a melee weapon. This allows equipped curios to dynamically
         * modify the damage dealt by the wearer.
         *
         * @param attacker The player who is performing the melee attack.
         * @param victim   The living entity being attacked.
         * @param weapon   The item stack representing the weapon used in the melee attack.
         * @param source   The damage source associated with the melee attack.
         * @param amount   The initial amount of damage dealt by the melee attack.
         * @return The final amount of damage after being modified by equipped curios, if applicable.
         */
        public static float applyOnWearerMeleeHit(Player attacker, LivingEntity victim, ItemStack weapon, DamageSource source, float amount) {
            float current = amount;
            Optional<ICuriosItemHandler> opt = CuriosApi.getCuriosInventory(attacker).resolve();
            if (opt.isPresent()) {
                for (SlotResult result : opt.get().findCurios(s -> s.getItem() instanceof BrutalityCurioItem)) {
                    current = ((BrutalityCurioItem) result.stack().getItem()).onWearerMeleeHit(attacker, weapon, result.stack(), victim, source, current);
                }
            }
            return current;
        }

        /**
         * Modifies the given dynamic attribute value by calculating additional bonuses
         * from all equipped curios of type {@link BrutalityCurioItem}. Each applicable item can
         * contribute a specific bonus to the provided attribute value.
         *
         * @implNote Do not get an attribute value while modifying the value, for example,
         * do not get the {@link net.minecraft.world.entity.ai.attributes.Attributes#MAX_HEALTH} of an entity if you plan to modify the bonus of {@code MAX_HEALTH}. Doing so will result in a {@link StackOverflowError}, surround with an if statement. Look at {@link net.goo.brutality.common.item.curios.anklet.AnkletOfTheImprisoned} for an example
         *
         * @param livingEntity The {@link LivingEntity} whose attribute value is being modified. This entity
         *                     is expected to wear any applicable curios.
         * @param attribute    The {@link Attribute} being modified. Each curio may contribute a dynamic
         *                     bonus to this attribute.
         * @param amount       The initial value of the attribute that is to be modified.
         * @return The total modified attribute value after applying bonuses from all applicable curios.
         */
        public static double modifyDynamicAttributeValues(LivingEntity livingEntity, Attribute attribute, double amount) {
            double total = amount;
            Optional<ICuriosItemHandler> opt = CuriosApi.getCuriosInventory(livingEntity).resolve();
            if (opt.isPresent()) {
                // Find all items that are BrutalityCurioItems
                for (SlotResult result : opt.get().findCurios(s -> s.getItem() instanceof BrutalityCurioItem)) {
                    BrutalityCurioItem item = (BrutalityCurioItem) result.stack().getItem();

                    // Add the item's specific bonus to our running total
                    total += item.getDynamicAttributeBonus(livingEntity, result.stack(), attribute, total);
                }
            }
            return total;
        }
    }
}
