package net.goo.brutality.item.weapon.tome;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.magic.SpellStorage;
import net.goo.brutality.magic.spells.voidwalker.GraviticImplosionSpell;
import net.goo.brutality.magic.spells.voidwalker.SpatialRuptureSpell;
import net.goo.brutality.magic.spells.voidwalker.VoidWalkSpell;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class VoidTome extends BaseMagicTome {

    public VoidTome(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        SpellStorage.addSpell(stack, new GraviticImplosionSpell(), 1);
        SpellStorage.addSpell(stack, new SpatialRuptureSpell(), 1);
        SpellStorage.addSpell(stack, new VoidWalkSpell(), 10);
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
                    ModAttributes.VOIDWALKER_SCHOOL_LEVEL.get(),
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