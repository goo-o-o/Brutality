package net.goo.brutality.item.curios.belt;

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

public class WarpsliceScabbard extends BrutalityCurioItem {
    public WarpsliceScabbard(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.BELT;
    }

    UUID WARPSLICE_LETHALITY_UUID = UUID.fromString("b0712359-d617-4fcf-9b44-e922b3d585c8");
    UUID WARPSLICE_ARMOR_PEN_UUID = UUID.fromString("ca3453e1-4d2a-4dd5-b4e9-4985cf3e34ae");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ModAttributes.LETHALITY.get(), new AttributeModifier(WARPSLICE_LETHALITY_UUID, "Lethality Buff", 7, AttributeModifier.Operation.ADDITION));
        builder.put(ModAttributes.ARMOR_PENETRATION.get(), new AttributeModifier(WARPSLICE_ARMOR_PEN_UUID, "Armor Pen Buff", 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
        return builder.build();
    }
}
