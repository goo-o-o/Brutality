package net.goo.brutality.common.mob_effect;

import net.goo.brutality.common.registry.BrutalityAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PrecisionEffect extends MobEffect {
    public static UUID PRECISION_UUID = UUID.fromString("0048a5ff-8970-4a0e-8f9e-b9f18d792a53");

    public PrecisionEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(BrutalityAttributes.CRITICAL_STRIKE_CHANCE.get(), String.valueOf(PRECISION_UUID), 0, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, @NotNull AttributeModifier modifier) {
        return (amplifier + 1) * 0.01F;
    }
}

