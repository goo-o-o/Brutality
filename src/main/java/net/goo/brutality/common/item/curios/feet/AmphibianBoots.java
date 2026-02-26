package net.goo.brutality.common.item.curios.feet;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeMod;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class AmphibianBoots extends BrutalityCurioItem {
    public AmphibianBoots(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public double getDynamicAttributeBonus(LivingEntity owner, ItemStack stack, Attribute attribute, double currentBonus) {
        if (attribute == Attributes.ATTACK_SPEED && owner.isInWater()) {
            return currentBonus * 1.5;
        }
        return super.getDynamicAttributeBonus(owner, stack, attribute, currentBonus);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ForgeMod.SWIM_SPEED.get(), new AttributeModifier(uuid, "Swim Speed Buff", 1, AttributeModifier.Operation.MULTIPLY_TOTAL));
        if (slotContext.entity() != null && slotContext.entity().level().isClientSide()) {
            if (slotContext.entity().isInWater()) {
                builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(uuid, "Attack Speed Buff", 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
        }
        return builder.build();
    }

}
