package net.goo.brutality.item.curios.anklet;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityAnkletItem;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class BloodClotAnklet extends BrutalityAnkletItem {


    public BloodClotAnklet(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.ANKLET;
    }

    UUID BLOOD_CLOT_ANKLET_DODGE_UUID = UUID.fromString("19fd15ac-669c-46bf-8130-65c437e845aa");
    UUID BLOOD_CLOT_ANKLET_LIFESTEAL_UUID = UUID.fromString("76876953-d17a-4c75-ba37-ca8b0cdcd701");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ModAttributes.DODGE_CHANCE.get(), new AttributeModifier(BLOOD_CLOT_ANKLET_DODGE_UUID, "Dodge Buff", 0.15, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(ModAttributes.LIFESTEAL.get(), new AttributeModifier(BLOOD_CLOT_ANKLET_LIFESTEAL_UUID, "Lifesteal Buff", 0.05, AttributeModifier.Operation.MULTIPLY_BASE));
            return builder.build();

        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }

}
