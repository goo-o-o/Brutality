package net.goo.brutality.item.curios.anklet;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
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

public class BasketballAnklet extends BrutalityAnkletItem {


    public BasketballAnklet(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID BASKETBALL_ANKLET_DODGE_UUID = UUID.fromString("cf8103d7-f467-4105-bc5d-1da97374054d");
    UUID BASKETBALL_ANKLET_JUMP_UUID = UUID.fromString("5244bedd-1a86-4a2d-b451-edcfeceb3c80");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ModAttributes.DODGE_CHANCE.get(),
                    new AttributeModifier(BASKETBALL_ANKLET_DODGE_UUID, "Dodge Buff", 0.167, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(ModAttributes.JUMP_HEIGHT.get(),
                    new AttributeModifier(BASKETBALL_ANKLET_JUMP_UUID, "Jump Buff", 2, AttributeModifier.Operation.ADDITION));
            return builder.build();
    }

}
