package net.goo.brutality.item.curios.hands;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.curios.base.BaseHandsCurio;
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

public class StygianChain extends BaseHandsCurio {


    public StygianChain(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID STYGIAN_CHAIN_ARMOR_UUID = UUID.fromString("ba0539da-ccb3-4970-a074-69aa9c6f687c");
    UUID STYGIAN_CHAIN_ARMOR_TOUGHNESS_UUID = UUID.fromString("3a665c21-2656-437c-84e7-e82b379f45f6");
    UUID STYGIAN_CHAIN_ARMOR_PEN_UUID = UUID.fromString("64dbb3b3-313b-4d2f-9e6d-d61b247685d0");
    UUID STYGIAN_CHAIN_LIFESTEAL_UUID = UUID.fromString("929d26fc-7b7c-4eae-a667-26668ab863a1");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(Attributes.ARMOR, new AttributeModifier(STYGIAN_CHAIN_ARMOR_UUID, "Armor Nerf", -6, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(STYGIAN_CHAIN_ARMOR_TOUGHNESS_UUID, "Armor Toughness Nerf", -4, AttributeModifier.Operation.ADDITION));
        builder.put(ModAttributes.ARMOR_PENETRATION.get(), new AttributeModifier(STYGIAN_CHAIN_ARMOR_PEN_UUID, "Armor Pen Buff", 0.15, AttributeModifier.Operation.MULTIPLY_BASE));
        builder.put(ModAttributes.LIFESTEAL.get(), new AttributeModifier(STYGIAN_CHAIN_LIFESTEAL_UUID, "Lifesteal Buff", 0.15, AttributeModifier.Operation.MULTIPLY_BASE));
        return builder.build();

    }
}
