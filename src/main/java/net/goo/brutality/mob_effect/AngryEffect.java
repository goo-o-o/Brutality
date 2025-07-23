package net.goo.brutality.mob_effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class AngryEffect extends MobEffect {

    public AngryEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);

        UUID ANGRY_AD_UUID = UUID.fromString("3a3fdccb-f138-47dd-a006-3333615d8651");
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, String.valueOf(ANGRY_AD_UUID), 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
        UUID ANGRY_ARMOR_UUID = UUID.fromString("d3ff1bfe-c9f7-44fe-aba8-d34d68526f48");
        this.addAttributeModifier(Attributes.ARMOR, String.valueOf(ANGRY_ARMOR_UUID), -0.7, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
