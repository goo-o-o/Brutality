package net.goo.brutality.common.mob_effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class HappyEffect extends MobEffect {


    public HappyEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        UUID HAPPY_MS_UUID = UUID.fromString("a5ff0c3a-1121-456d-8175-319d9c217a06");
        UUID HAPPY_AS_UUID = UUID.fromString("91cc9e3f-d1e2-416e-a99d-d53a63e7a504");
        UUID HAPPY_AD_UUID = UUID.fromString("907ffdab-7cee-489b-81cd-83f744759585");

        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, String.valueOf(HAPPY_MS_UUID), 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_SPEED, String.valueOf(HAPPY_AS_UUID), 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, String.valueOf(HAPPY_AD_UUID), -0.3, AttributeModifier.Operation.MULTIPLY_TOTAL);


    }

}
