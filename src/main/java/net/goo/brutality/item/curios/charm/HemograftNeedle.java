package net.goo.brutality.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class HemograftNeedle extends BrutalityCurioItem {


    public HemograftNeedle(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }

    UUID HEMOGRAFT_NEEDLE_LIFESTEAL_UUID = UUID.fromString("f7b979bf-8a39-4265-8c75-b0b466914a9d");
    UUID HEMOGRAFT_NEEDLE_CRIT_CHANCE_UUID = UUID.fromString("f7b979bf-8a39-4265-8c75-b0b466914a9d");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ModAttributes.LIFESTEAL.get(), new AttributeModifier(HEMOGRAFT_NEEDLE_LIFESTEAL_UUID,
                    "Lifesteal Boost", 0.05, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(ModAttributes.CRITICAL_STRIKE_CHANCE.get(), new AttributeModifier(HEMOGRAFT_NEEDLE_CRIT_CHANCE_UUID,
                    "Crit Chance Boost", 0.15, AttributeModifier.Operation.MULTIPLY_BASE));
            return builder.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }


}
