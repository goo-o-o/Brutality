package net.goo.brutality.common.mob_effect;

import net.goo.brutality.common.registry.BrutalityAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ArcaneSurge extends MobEffect {
    private static final UUID ARCANE_SURGE_UUID = UUID.fromString("f67edafa-653b-4cc3-a77b-45ce0a4fa9e9");

    public ArcaneSurge(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(BrutalityAttributes.SPELL_DAMAGE.get(), String.valueOf(ARCANE_SURGE_UUID), 0, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, @NotNull AttributeModifier modifier) {
        return (amplifier + 1) * 0.01F;
    }
}

