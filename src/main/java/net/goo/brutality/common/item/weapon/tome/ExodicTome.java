package net.goo.brutality.common.item.weapon.tome;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.UUID;

public class ExodicTome extends BaseMagicTome {



    UUID EXODIC_SCHOOL_BOOST_UUID = UUID.fromString("e36c1e30-0ca9-4b23-8251-398634bd9c42");

    public ExodicTome(Rarity rarity) {
        super(rarity);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);

        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(modifiers);
            builder.put(
                    BrutalityAttributes.EXODIC_SCHOOL_LEVEL.get(),
                    new AttributeModifier(
                            EXODIC_SCHOOL_BOOST_UUID,
                            "Exodic School bonus",
                            1,
                            AttributeModifier.Operation.ADDITION
                    )
            );

            return builder.build();
        }
        return modifiers;
    }
}