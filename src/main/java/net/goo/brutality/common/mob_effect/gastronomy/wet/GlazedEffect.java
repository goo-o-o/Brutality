package net.goo.brutality.common.mob_effect.gastronomy.wet;

import net.goo.brutality.common.mob_effect.gastronomy.IGastronomyEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class GlazedEffect extends MobEffect implements IGastronomyEffect {

    public GlazedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);

        UUID GLAZED_MS_UUID = UUID.fromString("11e58342-1a80-4de8-bcd4-e23771782edf");
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, String.valueOf(GLAZED_MS_UUID), -0.15, AttributeModifier.Operation.MULTIPLY_TOTAL);
        UUID GLAZED_AS_UUID = UUID.fromString("16f1157b-ef86-4140-8a65-5f1a7a4ff0ff");
        this.addAttributeModifier(Attributes.ATTACK_SPEED, String.valueOf(GLAZED_AS_UUID), -0.15, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration % 4 == 0;
    }

    @Override
    public IGastronomyEffect.Type getType() {
        return IGastronomyEffect.Type.WET;
    }

    @Override
    public float multiplierPerLevel() {
        return 0.1F;
    }

    @Override
    public boolean scalesWithLevel() {
        return true;
    }

    @Override
    public boolean modifiesDamage() {
        return true;
    }

    @Override
    public float baseMultiplier() {
        return 0.05F;
    }

}

