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

public class BlackMatterNecklace extends BrutalityCurioItem {


    public BlackMatterNecklace(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public double getDynamicAttributeBonus(LivingEntity owner, ItemStack stack, Attribute attribute, double currentBonus) {
        if (attribute == BrutalityAttributes.CRITICAL_STRIKE_CHANCE.get()) {
            return owner.getArmorValue() * 0.02F;
        }
        if (attribute == BrutalityAttributes.CRITICAL_STRIKE_DAMAGE.get()) {
            return owner.getArmorValue() * -0.01F;
        }
        return super.getDynamicAttributeBonus(owner, stack, attribute, currentBonus);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        if (slotContext.entity() != null && slotContext.entity().level().isClientSide()) {

            float armor = slotContext.entity().getArmorValue();
            builder.put(BrutalityAttributes.CRITICAL_STRIKE_CHANCE.get(),
                    new AttributeModifier(uuid, "Crit Chance Buff", armor * 0.02F, AttributeModifier.Operation.ADDITION));
            builder.put(BrutalityAttributes.CRITICAL_STRIKE_DAMAGE.get(),
                    new AttributeModifier(uuid, "Crit Damage Buff", armor * -0.01F, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }

}
