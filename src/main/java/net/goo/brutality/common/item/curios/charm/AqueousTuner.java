package net.goo.brutality.common.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class AqueousTuner extends BrutalityCurioItem {
    public AqueousTuner(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public double getDynamicAttributeBonus(SlotContext slotContext, ItemStack stack, AttributeInstance attributeInstance, double currentBonus) {
        if (attributeInstance.getAttribute() == BrutalityAttributes.SPELL_DAMAGE.get()) {
            if (slotContext.entity().isInWaterRainOrBubble())
                return 0.25;
        }
        return super.getDynamicAttributeBonus(slotContext, stack, attributeInstance, currentBonus);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null && slotContext.entity().level().isClientSide()) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            LivingEntity livingEntity = slotContext.entity();

            if (livingEntity.isInWaterRainOrBubble()) {
                builder.put(BrutalityAttributes.SPELL_DAMAGE.get(), new AttributeModifier(uuid, "Spell Dmg", 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
            return builder.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }
}
