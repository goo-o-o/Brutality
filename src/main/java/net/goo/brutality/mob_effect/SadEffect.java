package net.goo.brutality.mob_effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class SadEffect extends MobEffect {


    public SadEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        UUID HAPPY_AS_UUID = UUID.fromString("bbd5b575-f9d8-43d5-8ad7-95cf06de9558");
        UUID HAPPY_MS_UUID = UUID.fromString("bf084039-eea6-4ce7-83d1-ca5733c6255f");

        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, String.valueOf(HAPPY_MS_UUID), -0.35, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ARMOR, String.valueOf(HAPPY_AS_UUID), 0.35, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
