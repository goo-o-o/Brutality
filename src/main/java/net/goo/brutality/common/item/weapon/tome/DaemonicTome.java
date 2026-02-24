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

public class DaemonicTome extends BaseMagicTome {



    UUID DAEMONIC_SCHOOL_BOOST_UUID = UUID.fromString("c9e41989-b8f4-47da-b858-92c5682e5b8c");

    public DaemonicTome(Rarity rarity, int baseSpellSlots, int baseAugmentSlots) {
        super(rarity, baseSpellSlots, baseAugmentSlots);
    }


    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);

        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(modifiers);
            builder.put(
                    BrutalityAttributes.DAEMONIC_SCHOOL_LEVEL.get(),
                    new AttributeModifier(
                            DAEMONIC_SCHOOL_BOOST_UUID,
                            "Daemonic School bonus",
                            1,
                            AttributeModifier.Operation.ADDITION
                    )
            );

            return builder.build();
        }
        return modifiers;
    }

}