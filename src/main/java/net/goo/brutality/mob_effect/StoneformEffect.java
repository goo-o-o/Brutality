package net.goo.brutality.mob_effect;

import net.goo.brutality.registry.ModAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class StoneformEffect extends MobEffect {
    UUID ARMOR_TOUGHNESS_UUID = UUID.fromString("e3380b8e-6d08-4919-b2d6-952d2c0191b8");
    UUID MOVEMENT_SPEED_UUID = UUID.fromString("4920f989-2ffa-4b63-a8dd-b69eaa36c83b");
    UUID DAMAGE_TAKEN_UUID = UUID.fromString("ed60f1dd-8976-44fd-ab33-721ecfeb3b41");

    public StoneformEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);

        this.addAttributeModifier(
                Attributes.ARMOR_TOUGHNESS,
                ARMOR_TOUGHNESS_UUID.toString(),
                10.0D,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );

        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                MOVEMENT_SPEED_UUID.toString(),
                -10.0D,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
        this.addAttributeModifier(
                ModAttributes.DAMAGE_TAKEN.get(),
                DAMAGE_TAKEN_UUID.toString(),
                -0.5,
                AttributeModifier.Operation.MULTIPLY_BASE
        );


    }
}
