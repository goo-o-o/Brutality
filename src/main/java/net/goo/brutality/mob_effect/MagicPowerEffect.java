package net.goo.brutality.mob_effect;

import net.goo.brutality.registry.ModAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MagicPowerEffect extends MobEffect {
    public static UUID MAGIC_POWER_UUID = UUID.fromString("f67edafa-653b-4cc3-a77b-45ce0a4fa9e9");

    public MagicPowerEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(ModAttributes.SPELL_DAMAGE.get(), String.valueOf(MAGIC_POWER_UUID), 0, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, @NotNull AttributeModifier modifier) {
        return (amplifier + 1) * 0.01F;
    }
}

