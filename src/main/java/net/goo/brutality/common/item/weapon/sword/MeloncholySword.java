package net.goo.brutality.common.item.weapon.sword;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.base.BrutalitySwordItem;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.ForgeMod;

import java.util.UUID;

public class MeloncholySword extends BrutalitySwordItem {

    public MeloncholySword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 100;
    }


    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        entity.level().playSound(null, entity.getOnPos(), BrutalitySounds.SQUELCH.get(), SoundSource.PLAYERS, 10F, Mth.nextFloat(entity.getRandom(), 0.8F, 1.2F));
        return super.onEntitySwing(stack, entity);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pAttacker.level().playSound(null, pAttacker.getOnPos(), BrutalitySounds.SQUELCH.get(), SoundSource.PLAYERS, 10F, Mth.nextFloat(pAttacker.getRandom(), 0.8F, 1.2F));
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    UUID MELONCHOLY_RANGE_UUID = UUID.fromString("cf4d35ec-1bc4-482f-89a9-6bd2d43d2c25");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);

        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(modifiers);
            builder.put(
                    ForgeMod.ENTITY_REACH.get(),
                    new AttributeModifier(
                            MELONCHOLY_RANGE_UUID,
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
