package net.goo.brutality.mob_effect;

import net.goo.brutality.registry.BrutalityModAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ManaFatigueEffect extends MobEffect {
    public static UUID MANA_FATIGUE_UUID = UUID.fromString("dd38c3e9-0e9d-465f-bce6-057cb2d5267a");
    public ManaFatigueEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(BrutalityModAttributes.MANA_COST.get(), String.valueOf(MANA_FATIGUE_UUID), 0, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, @NotNull AttributeModifier modifier) {
        return (amplifier + 1) * 0.01F;
    }
}

