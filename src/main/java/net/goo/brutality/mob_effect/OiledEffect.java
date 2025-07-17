package net.goo.brutality.mob_effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class SlickedEffect extends MobEffect {

    public SlickedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);

        UUID SLICKED_MS_UUID = UUID.fromString("9b5dd583-f667-4fac-a147-05756a8559fd");
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, String.valueOf(SLICKED_MS_UUID), -0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);

    }


}

