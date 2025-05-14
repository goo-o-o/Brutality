package net.goo.armament.mob_effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class StoneformEffect extends MobEffect {
    private static final UUID KNOCKBACK_RESISTANCE_UUID = UUID.fromString("beac5e62-3b59-4cf6-aad1-192472e3f0a7");

    public StoneformEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);

        this.addAttributeModifier(
                Attributes.KNOCKBACK_RESISTANCE,
                KNOCKBACK_RESISTANCE_UUID.toString(),
                1.0D,
                AttributeModifier.Operation.ADDITION
        );
    }
}
