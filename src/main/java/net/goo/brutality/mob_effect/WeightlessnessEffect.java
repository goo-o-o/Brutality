package net.goo.brutality.mob_effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;

public class WeightlessnessEffect extends MobEffect {
    public static String WEIGHTLESSNESS_GRAVITY_UUID = "9529c4cd-5426-44b7-91c4-1e5f9019c53a";
    protected final double multiplier;

    public WeightlessnessEffect(MobEffectCategory category, int color, double multiplier) {
        super(category, color);
        this.multiplier = multiplier;
        this.addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), WEIGHTLESSNESS_GRAVITY_UUID, 0, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, AttributeModifier modifier) {
        return this.multiplier * (amplifier + 1);
    }
}