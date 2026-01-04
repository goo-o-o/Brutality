package net.goo.brutality.item.curios.heart;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.curios.base.BaseHeartCurio;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class BoxOfChocolates extends BaseHeartCurio {


    public BoxOfChocolates(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID BOX_OF_CHOCOLATES_VISIBILITY = UUID.fromString("59d9cded-4a6a-4d6e-86b1-861e83a4cfa1");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ModAttributes.ENTITY_VISIBILITY.get(),
                new AttributeModifier(BOX_OF_CHOCOLATES_VISIBILITY, "Entity Visibility Buff", -0.15, AttributeModifier.Operation.MULTIPLY_BASE));

        return builder.build();

    }
}
