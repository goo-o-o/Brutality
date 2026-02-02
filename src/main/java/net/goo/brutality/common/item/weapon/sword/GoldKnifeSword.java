package net.goo.brutality.common.item.weapon.sword;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.base.BrutalitySwordItem;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.ForgeMod;

import java.util.List;
import java.util.UUID;

public class GoldKnifeSword extends BrutalitySwordItem {


    public GoldKnifeSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }



    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(1, pAttacker, (attacker) -> {
            attacker.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });

        if (pTarget.hasEffect(BrutalityEffects.SCORED.get())) {
            int amplifier = pTarget.getEffect(BrutalityEffects.SCORED.get()).getAmplifier();

            if (amplifier > 2) return true;

            pTarget.addEffect(new MobEffectInstance(BrutalityEffects.SCORED.get(), 60, amplifier + 1));
        }
        pTarget.addEffect(new MobEffectInstance(BrutalityEffects.SCORED.get(), 60, 0));

        return true;
    }

    UUID GOLD_KNIFE_RANGE_UUID = UUID.fromString("57667a41-a6c7-4412-aced-64ded0e9ded9");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);

        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(modifiers);
            builder.put(
                    ForgeMod.ENTITY_REACH.get(),
                    new AttributeModifier(
                            GOLD_KNIFE_RANGE_UUID,
                            "Reach bonus",
                            2,
                            AttributeModifier.Operation.ADDITION
                    )
            );

            return builder.build();
        }
        return modifiers;
    }
}
