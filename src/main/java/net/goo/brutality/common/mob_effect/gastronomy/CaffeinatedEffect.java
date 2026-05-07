package net.goo.brutality.common.mob_effect.gastronomy;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class CaffeinatedEffect extends GastronomyEffect {
    private final static UUID CAFFEINATED_MS_UUID = UUID.fromString("5ad329a4-7627-4537-9d0e-0ebb8f52e94a");
    private final static UUID CAFFEINATED_AS_UUID = UUID.fromString("1ce1830e-aac1-4b99-b072-448f3784daf3");


    public CaffeinatedEffect(MobEffectCategory pCategory, Type type, float baseMult, float perLevelMult) {
        super(pCategory, type, baseMult, perLevelMult);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, String.valueOf(CAFFEINATED_MS_UUID), 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_SPEED, String.valueOf(CAFFEINATED_AS_UUID), 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}

