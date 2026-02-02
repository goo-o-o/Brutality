package net.goo.brutality.common.item.curios.charm;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
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

public class Gluttony extends BrutalityCurioItem {
    public static final String SOULS = "souls";

    public Gluttony(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public double getDynamicAttributeBonus(LivingEntity owner, ItemStack stack, Attribute attribute, double currentBonus) {
        if (attribute == Attributes.ATTACK_DAMAGE) {
            return stack.getOrCreateTag().getInt(SOULS) * 0.01;
        }
        return super.getDynamicAttributeBonus(owner, stack, attribute, currentBonus);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null && slotContext.entity().level().isClientSide()) {
            ImmutableMap.Builder<Attribute, AttributeModifier> builder = ImmutableMap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "AD buff", stack.getOrCreateTag().getInt(SOULS) * 0.01, AttributeModifier.Operation.ADDITION));

        }

        return super.getAttributeModifiers(slotContext, uuid, stack);
    }


}

