package net.goo.brutality.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class ChocolateBar extends BrutalityCurioItem {


    public ChocolateBar(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }

    UUID CHOCOBAR_MS = UUID.fromString("78730b04-de4d-4f55-9d9c-d771150623f0");
    UUID CHOCOBAR_AS = UUID.fromString("5adb088b-d59d-4865-b13f-b67584c3d139");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(CHOCOBAR_MS,
                "MS Buff", 0.15F, AttributeModifier.Operation.MULTIPLY_TOTAL));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(CHOCOBAR_AS,
                "AS Buff", 0.15F, AttributeModifier.Operation.MULTIPLY_TOTAL));
        return builder.build();
    }
}
