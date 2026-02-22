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

public class DarkistTome extends BaseMagicTome {


    UUID DARKIST_SCHOOL_BOOST_UUID = UUID.fromString("e4851548-661d-463a-905f-a0c28426c341");

    public DarkistTome(Rarity rarity, int baseSpellSlots, int baseAugmentSlots) {
        super(rarity, baseSpellSlots, baseAugmentSlots);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);

        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(modifiers);
            builder.put(
                    BrutalityAttributes.DARKIST_SCHOOL_LEVEL.get(),
                    new AttributeModifier(
                            DARKIST_SCHOOL_BOOST_UUID,
                            "Darkist School bonus",
                            1,
                            AttributeModifier.Operation.ADDITION
                    )
            );

            return builder.build();
        }
        return modifiers;
    }

}