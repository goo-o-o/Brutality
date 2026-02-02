package net.goo.brutality.common.item.curios.heart;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class BrutalHeart extends BrutalityCurioItem {

    public BrutalHeart(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public double getDynamicAttributeBonus(LivingEntity owner, ItemStack stack, Attribute attribute, double currentBonus) {
        if (attribute == BrutalityAttributes.CRITICAL_STRIKE_CHANCE.get()) {
            float fromThisCurio = owner.getMaxHealth();
            float original = fromThisCurio * (1 / 0.75F);
            return (original - fromThisCurio) * 0.05;
        }
        return super.getDynamicAttributeBonus(owner, stack, attribute, currentBonus);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, "Health Debuff", -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL));
        builder.put(BrutalityAttributes.CRITICAL_STRIKE_DAMAGE.get(), new AttributeModifier(uuid, "Crit Damage Buff", 0.25, AttributeModifier.Operation.ADDITION));
        if (slotContext.entity() != null && slotContext.entity().level().isClientSide()) {
            LivingEntity livingEntity = slotContext.entity();
            // Convert health lost from THIS CURIO at a ratio of 1hp = 5% crit
            // AttributeInstance healthInstance = livingEntity.getAttribute(Attributes.MAX_HEALTH);
            // Easier to just use a 1.25/1 ratio

            float alteredHealth = livingEntity.getMaxHealth();
            float originalHealth = alteredHealth * (1 / 0.75F);
            float healthDiff = originalHealth - alteredHealth;

            builder.put(BrutalityAttributes.CRITICAL_STRIKE_CHANCE.get(), new AttributeModifier(uuid, "Crit Chance Buff", healthDiff * 0.05, AttributeModifier.Operation.ADDITION));

        }

        return builder.build();
    }

}
