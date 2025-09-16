package net.goo.brutality.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class BloodPack extends BrutalityCurioItem {
    public BloodPack(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }

    UUID BLOOD_PACK_MAX_HEALTH_UUID = UUID.fromString("d21a406e-f156-4d7f-b2a4-a7383aef1ee9");
    UUID BLOOD_PACK_LIFESTEAL_UUID = UUID.fromString("e220ee86-9e2e-45c4-8afc-89d31224d3ac");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(Attributes.MAX_HEALTH, new AttributeModifier(BLOOD_PACK_MAX_HEALTH_UUID, "Health Nerf", -3, AttributeModifier.Operation.ADDITION));
        builder.put(ModAttributes.LIFESTEAL.get(), new AttributeModifier(BLOOD_PACK_LIFESTEAL_UUID, "Lifesteal Buff", 0.125, AttributeModifier.Operation.MULTIPLY_BASE));
        return builder.build();
    }
}
