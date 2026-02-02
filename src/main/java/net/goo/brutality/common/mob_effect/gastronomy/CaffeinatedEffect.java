package net.goo.brutality.common.mob_effect.gastronomy;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class CaffeinatedEffect extends MobEffect implements IGastronomyEffect {

    public CaffeinatedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        UUID CAFFEINATED_MS_UUID = UUID.fromString("5ad329a4-7627-4537-9d0e-0ebb8f52e94a");
        UUID CAFFEINATED_AS_UUID = UUID.fromString("1ce1830e-aac1-4b99-b072-448f3784daf3");

        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, String.valueOf(CAFFEINATED_MS_UUID), 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_SPEED, String.valueOf(CAFFEINATED_AS_UUID), 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration % 4 == 0;
    }

    @Override
    public Type getType() {
        return Type.WET;
    }

    @Override
    public boolean scalesWithLevel() {
        return false;
    }

    @Override
    public boolean modifiesDamage() {
        return false;
    }

    @Override
    public float baseMultiplier() {
        return 0.05F;
    }

    @Override
    public float multiplierPerLevel() {
        return 0;
    }

}

