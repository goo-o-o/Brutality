package net.goo.brutality.common.item.weapon.sword;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.base.BrutalitySwordItem;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HellspecCleaver extends BrutalitySwordItem {



    UUID HELLSPEC_CLEAVER_UUID = UUID.fromString("9a389c9b-3626-444f-8233-c134ca09a646");

    public HellspecCleaver(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, List.of());
    }



    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> original = super.getAttributeModifiers(slot, stack);

        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> newAttributes = ImmutableMultimap.builder();
            newAttributes.putAll(original);
            newAttributes.put(BrutalityAttributes.ARMOR_PENETRATION.get(),
                    new AttributeModifier(HELLSPEC_CLEAVER_UUID, "Armor pen buff", 0.5, AttributeModifier.Operation.MULTIPLY_BASE));

            return newAttributes.build();
        }

        return super.getAttributeModifiers(slot, stack);
    }

}
