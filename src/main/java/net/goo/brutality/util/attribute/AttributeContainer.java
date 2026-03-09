package net.goo.brutality.util.attribute;

import net.goo.brutality.Brutality;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class AttributeContainer {
    protected final Attribute attribute;
    protected final double value;
    protected final AttributeModifier.Operation operation;

    public AttributeContainer(Attribute attribute, double value, AttributeModifier.Operation operation) {
        this.attribute = attribute;
        this.value = value;
        this.operation = operation;
    }

    public AttributeModifier createModifier(UUID uuid) {
        return new AttributeModifier(
                uuid,
                String.format("%s_%s_modifier", Brutality.MOD_ID, attribute.getDescriptionId()),
                value,
                operation
        );
    }

    // Getters for use in Tooltip rendering
    public Attribute attribute() { return attribute; }
    public double value() { return value; }
    public AttributeModifier.Operation operation() { return operation; }
}