package net.goo.brutality.item.weapon.sword;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.Brutality;
import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.registry.BrutalityModAttributes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HellspecCleaver extends BrutalitySwordItem {

    public HellspecCleaver(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity);
    }

    UUID HELLSPEC_CLEAVER_UUID = UUID.fromString("9a389c9b-3626-444f-8233-c134ca09a646");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> original = super.getAttributeModifiers(slot, stack);

        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> newAttributes = ImmutableMultimap.builder();
            newAttributes.putAll(original);
            newAttributes.put(BrutalityModAttributes.ARMOR_PENETRATION.get(),
                    new AttributeModifier(HELLSPEC_CLEAVER_UUID, "Armor pen buff", 0.5, AttributeModifier.Operation.MULTIPLY_BASE));

            return newAttributes.build();
        }

        return super.getAttributeModifiers(slot, stack);
    }

}
