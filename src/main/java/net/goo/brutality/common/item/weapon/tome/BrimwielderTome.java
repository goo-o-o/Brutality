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

public class BrimwielderTome extends BaseMagicTome {



    UUID BRIMWIELDER_SCHOOL_BOOST_UUID = UUID.fromString("e5f08d24-acbf-4ec5-a19f-33977d4245e9");

    public BrimwielderTome(Rarity rarity) {
        super(rarity);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);

        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(modifiers);
            builder.put(
                    BrutalityAttributes.BRIMWIELDER_SCHOOL_LEVEL.get(),
                    new AttributeModifier(
                            BRIMWIELDER_SCHOOL_BOOST_UUID,
                            "Brimwielder School bonus",
                            1,
                            AttributeModifier.Operation.ADDITION
                    )
            );

            return builder.build();
        }
        return modifiers;
    }
}