package net.goo.brutality.common.item.curios.necklace;

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

public class BloodHowlPendant extends BrutalityCurioItem {


    public BloodHowlPendant(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public double getDynamicAttributeBonus(LivingEntity owner, ItemStack stack, Attribute attribute, double currentBonus) {
        if (attribute == BrutalityAttributes.DAMAGE_TO_RAGE_RATIO.get()) {
            if (owner.getHealth() / owner.getMaxHealth() <= 0.5F)
                return 0.5F;
        }
        return super.getDynamicAttributeBonus(owner, stack, attribute, currentBonus);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null && slotContext.entity().level().isClientSide()) {
            if (slotContext.entity().getHealth() / slotContext.entity().getMaxHealth() < 0.5F) {
                ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
                builder.put(BrutalityAttributes.DAMAGE_TO_RAGE_RATIO.get(),
                        new AttributeModifier(uuid,
                                "Rage Gain Buff",
                                0.5,
                                AttributeModifier.Operation.MULTIPLY_TOTAL
                        ));
                return builder.build();
            }
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }
}
