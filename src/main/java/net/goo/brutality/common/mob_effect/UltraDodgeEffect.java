package net.goo.brutality.common.mob_effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class UltraDodgeEffect extends MobEffect {
    UUID ULTRA_DODGE_UUID = UUID.fromString("eb55d10b-019e-43c9-8cfd-3a76c44c410b");

    public UltraDodgeEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(Attributes.ARMOR, String.valueOf(ULTRA_DODGE_UUID), 100, AttributeModifier.Operation.ADDITION);
    }

}

