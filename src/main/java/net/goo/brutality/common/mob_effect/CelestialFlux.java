package net.goo.brutality.common.mob_effect;

import net.goo.brutality.common.registry.BrutalityAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CelestialFlux extends MobEffect {
    private static final UUID CELESTIAL_FLUX_UUID = UUID.fromString("591d5e78-a28a-45af-87fc-d958738c3731");
    public CelestialFlux(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(BrutalityAttributes.MANA_REGEN.get(), String.valueOf(CELESTIAL_FLUX_UUID), 0, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, @NotNull AttributeModifier modifier) {
        return (amplifier + 1) * 0.1F;
    }
}

