package net.goo.brutality.util.attribute;

import net.goo.brutality.Brutality;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public record AttributeContainer(Attribute attribute, double value, AttributeModifier.Operation operation) {
    public AttributeModifier createModifier(UUID uuid) {
        return new AttributeModifier(uuid, String.format("%s_%s_modifier", Brutality.MOD_ID, attribute.getDescriptionId()), value, operation);
    }
}