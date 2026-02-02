package net.goo.brutality.common.item.weapon.tome;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.common.registry.BrutalitySpells;
import net.goo.brutality.util.magic.SpellStorage;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class VoidTome extends BaseMagicTome {


    public VoidTome(Rarity rarity) {
        super(rarity);
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        SpellStorage.addSpell(stack, BrutalitySpells.GRAVITIC_IMPLOSION.get(), 1);
        SpellStorage.addSpell(stack, BrutalitySpells.SPATIAL_RUPTURE.get(), 1);
        SpellStorage.addSpell(stack, BrutalitySpells.VOID_WALK.get(), 10);
        return stack;
    }


    UUID VOID_SCHOOL_BOOST_UUID = UUID.fromString("bd93de15-b184-4c3f-a000-ccae764754cd");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);

        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(modifiers);
            builder.put(
                    BrutalityAttributes.VOIDWALKER_SCHOOL_LEVEL.get(),
                    new AttributeModifier(
                            VOID_SCHOOL_BOOST_UUID,
                            "Void School bonus",
                            1,
                            AttributeModifier.Operation.ADDITION
                    )
            );

            return builder.build();
        }
        return modifiers;
    }
}