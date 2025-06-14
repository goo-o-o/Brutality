package net.goo.brutality.mob_effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import static net.goo.brutality.util.ModResources.*;

public class HappyEffect extends MobEffect {


    public HappyEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);

        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, String.valueOf(BASE_MOVEMENT_SPEED_UUID), 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_SPEED, String.valueOf(BASE_ATTACK_SPEED_FOR_EFFECT_UUID), 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, String.valueOf(BASE_ATTACK_DAMAGE_FOR_EFFECT_UUID), -0.3, AttributeModifier.Operation.MULTIPLY_TOTAL);


    }

}
