package net.goo.brutality.common.mob_effect.gastronomy;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class GlazedEffect extends GastronomyEffect {
    private static final UUID GLAZED_MS_UUID = UUID.fromString("11e58342-1a80-4de8-bcd4-e23771782edf");
    private static final UUID GLAZED_AS_UUID = UUID.fromString("16f1157b-ef86-4140-8a65-5f1a7a4ff0ff");

    public GlazedEffect(MobEffectCategory pCategory, Type type, float baseMult, float perLevelMult) {
        super(pCategory, type, baseMult, perLevelMult);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, String.valueOf(GLAZED_MS_UUID), -0.15, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_SPEED, String.valueOf(GLAZED_AS_UUID), -0.15, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}

