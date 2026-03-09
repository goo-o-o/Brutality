package net.goo.brutality.util.attribute;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class SlottedAttributeContainer extends AttributeContainer {
    private final EquipmentSlot[] slots;

    public SlottedAttributeContainer(Attribute attribute, double value, AttributeModifier.Operation operation, EquipmentSlot... slots) {
        super(attribute, value, operation); // Pass core data to parent constructor
        this.slots = slots;
    }

    public EquipmentSlot[] slots() {
        return slots;
    }
}