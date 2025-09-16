package net.goo.brutality.item.curios.hands;

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

public class BloodPulseGauntlets extends BrutalityCurioItem {
    public BloodPulseGauntlets(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.HANDS;
    }

    UUID BLOOD_PULSE_RAGE_LEVEL_UUID = UUID.fromString("64f0a078-b7a3-4956-b0cf-fd02fdb9d0a6");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ModAttributes.RAGE_LEVEL.get(), new AttributeModifier(BLOOD_PULSE_RAGE_LEVEL_UUID, "Rage Level Buff", 2, AttributeModifier.Operation.ADDITION));
        return builder.build();
    }

}
