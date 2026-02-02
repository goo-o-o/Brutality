package net.goo.brutality.common.mob_effect;

import net.goo.brutality.common.registry.BrutalityAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MagicSicknessEffect extends MobEffect {
    public static UUID MAGIC_SICKNESS_UUID = UUID.fromString("8648d591-d944-4b76-b19d-adbaa04b1e1a");

    public MagicSicknessEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(BrutalityAttributes.SPELL_DAMAGE.get(), String.valueOf(MAGIC_SICKNESS_UUID), 0, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, @NotNull AttributeModifier modifier) {
        return (amplifier + 1) * 0.01F;
    }
}

