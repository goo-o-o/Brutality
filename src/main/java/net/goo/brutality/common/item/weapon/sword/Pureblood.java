package net.goo.brutality.common.item.weapon.sword;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.base.BrutalitySwordItem;
import net.goo.brutality.mixin.accessors.MobEffectInstanceSourceAccessor;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;

import java.util.List;
import java.util.UUID;

public class Pureblood extends BrutalitySwordItem {
    public Pureblood(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    UUID PUREBLOOD_UUID = UUID.fromString("e65f07f4-3d7d-4ff2-880e-a2f73af3e7ac");


    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> original = super.getAttributeModifiers(slot, stack);

        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> newAttributes = ImmutableMultimap.builder();
            newAttributes.putAll(original);
            newAttributes.put(BrutalityAttributes.OMNIVAMP.get(),
                    new AttributeModifier(PUREBLOOD_UUID, "Omnivamp buff", 0.1, AttributeModifier.Operation.ADDITION));

            return newAttributes.build();
        }

        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        MobEffectInstance instance = new MobEffectInstance(BrutalityEffects.SIPHONED.get(), 60, 0, false, false, true);
        ((MobEffectInstanceSourceAccessor) instance).brutality$setSourceID(attacker.getId());
        target.addEffect(instance);

        return super.hurtEnemy(stack, target, attacker);
    }
}
