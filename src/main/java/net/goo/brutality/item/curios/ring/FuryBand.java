package net.goo.brutality.item.curios.ring;

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

public class FuryBand extends BrutalityCurioItem {


    public FuryBand(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.RING;
    }

    UUID FURY_BAND_RAGE_TIME_UUID = UUID.fromString("19f09575-a3df-425d-9ba1-3889a6c51290");
    UUID FURY_BAND_RAGE_LEVEL_UUID = UUID.fromString("87c3e0a7-c9ea-4cd6-927d-a17509903caf");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ModAttributes.RAGE_TIME_MULTIPLIER.get(), new AttributeModifier(FURY_BAND_RAGE_TIME_UUID, "Max Rage Time Buff", 0.75, AttributeModifier.Operation.MULTIPLY_TOTAL));
        builder.put(ModAttributes.RAGE_LEVEL.get(), new AttributeModifier(FURY_BAND_RAGE_LEVEL_UUID, "Rage Level Buff", 1, AttributeModifier.Operation.ADDITION));
        return builder.build();

    }

}
