package net.goo.brutality.common.mob_effect.gastronomy;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class CandiedEffect extends GastronomyEffect {
   private static final UUID CANDIED_ARMOR_UUID = UUID.fromString("2a0eeca1-5f47-4888-9f74-de3640374003");

    public CandiedEffect(MobEffectCategory pCategory, Type type, float baseMult, float perLevelMult) {
        super(pCategory, type, baseMult, perLevelMult);
        this.addAttributeModifier(Attributes.ARMOR, String.valueOf(CANDIED_ARMOR_UUID), -0.15, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}

