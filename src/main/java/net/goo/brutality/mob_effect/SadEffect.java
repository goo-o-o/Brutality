package net.goo.brutality.mob_effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import static net.goo.brutality.util.ModResources.*;

public class SadEffect extends MobEffect {


    public SadEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);

        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, String.valueOf(BASE_MOVEMENT_SPEED_UUID), -0.35, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ARMOR, String.valueOf(BASE_ATTACK_SPEED_FOR_EFFECT_UUID), 0.35, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
