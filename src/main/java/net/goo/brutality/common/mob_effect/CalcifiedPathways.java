package net.goo.brutality.common.mob_effect;

import net.goo.brutality.common.registry.BrutalityAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CalcifiedPathways extends MobEffect {
    private static final UUID CALCIFIED_PATHWAYS_UUID = UUID.fromString("090aa673-694c-429a-ab44-e21dd78fa164");
    public CalcifiedPathways(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(BrutalityAttributes.MANA_REGEN.get(), String.valueOf(CALCIFIED_PATHWAYS_UUID), 0, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, @NotNull AttributeModifier modifier) {
        return (amplifier + 1) * -0.1F;
    }
}

