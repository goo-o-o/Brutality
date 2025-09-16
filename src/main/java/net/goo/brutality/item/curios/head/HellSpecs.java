package net.goo.brutality.item.curios.head;

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

public class HellSpecs extends BrutalityCurioItem {


    public HellSpecs(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.HEAD;
    }


    UUID HELLSPECS_RAGE_GAIN_UUID = UUID.fromString("78427d4f-3961-4424-82e0-87e7746a67cc");
    UUID HELLSPECS_BRIMWIELDER_SCHOOL_LEVEL = UUID.fromString("c6df6d40-218d-4417-b6b9-5d4549e9891d");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ModAttributes.RAGE_GAIN_MULTIPLIER.get(), new AttributeModifier(HELLSPECS_RAGE_GAIN_UUID, "Rage Gain Buff", 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL));
            builder.put(ModAttributes.BRIMWIELDER_SCHOOL_LEVEL.get(), new AttributeModifier(HELLSPECS_BRIMWIELDER_SCHOOL_LEVEL, "Brimwielder Buff", 1, AttributeModifier.Operation.ADDITION));
            return builder.build();
    }
}
