package net.goo.brutality.mob_effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class StunnedEffect extends MobEffect {


    public StunnedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);

        UUID STUNNED_MS_UUID = UUID.fromString("6db800b6-cc92-40c5-b7ff-c31fb452ec69");

        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, String.valueOf(STUNNED_MS_UUID), 0, AttributeModifier.Operation.MULTIPLY_TOTAL);

    }
}
