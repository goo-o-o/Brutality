package net.goo.brutality.common.item.curios.heart;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class RuneOfDelta extends BrutalityCurioItem {
    public RuneOfDelta(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public double getDynamicAttributeBonus(LivingEntity owner, ItemStack stack, Attribute attribute, double currentBonus) {
        if (attribute == BrutalityAttributes.MANA_REGEN.get()) {
            return Math.max(0, 7 - owner.level().getMaxLocalRawBrightness(owner.getOnPos().above()));
        }
        return super.getDynamicAttributeBonus(owner, stack, attribute, currentBonus);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(BrutalityAttributes.MAX_MANA.get(), new AttributeModifier(uuid, "Max Mana Buff", 20, AttributeModifier.Operation.ADDITION));
        if (slotContext.entity() != null && slotContext.entity().level().isClientSide()) {
            int lightLevel = slotContext.entity().level().getMaxLocalRawBrightness(slotContext.entity().getOnPos().above());
            int bonus = Math.max(0, 7 - lightLevel);
            if (bonus > 0)
                builder.put(BrutalityAttributes.MANA_REGEN.get(), new AttributeModifier(uuid, "Mana Regen Buff", bonus, AttributeModifier.Operation.ADDITION));

        }

        return builder.build();
    }
}
