package net.goo.brutality.common.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class BoilingBlood extends BrutalityCurioItem {
    public BoilingBlood(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public double getDynamicAttributeBonus(LivingEntity owner, ItemStack stack, Attribute attribute, double currentBonus) {
        if (attribute == BrutalityAttributes.DAMAGE_TAKEN.get()) {
            return owner.hasEffect(BrutalityEffects.ENRAGED.get()) ? 0.25F : 0;
        }
        return super.getDynamicAttributeBonus(owner, stack, attribute, currentBonus);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(BrutalityAttributes.DAMAGE_TO_RAGE_RATIO.get(), new AttributeModifier(uuid, "Rage Buff", 0.6, AttributeModifier.Operation.ADDITION));
        if (slotContext.entity() != null && slotContext.entity().level().isClientSide()) {
            if (slotContext.entity().hasEffect(BrutalityEffects.ENRAGED.get()))
                builder.put(BrutalityAttributes.DAMAGE_TAKEN.get(), new AttributeModifier(uuid, "Damage Taken Nerf", 0.25F, AttributeModifier.Operation.ADDITION));
        }
        return builder.build();
    }
}
