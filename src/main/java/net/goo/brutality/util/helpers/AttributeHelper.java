package net.goo.brutality.util.helpers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

public class AttributeHelper {
    public static void removeModifier(ItemStack pStack, UUID id) {
        if (!pStack.hasTag() || !pStack.getOrCreateTag().contains("AttributeModifiers", 9)) {
            return; // No attribute modifiers exist
        }

        ListTag attributesList = pStack.getOrCreateTag().getList("AttributeModifiers", 10);
        boolean removed = false;

        for (int i = 0; i < attributesList.size(); i++) {
            CompoundTag modifierTag = attributesList.getCompound(i);
            UUID modifierId = modifierTag.getUUID("UUID");

            if (modifierId.equals(id)) {
                attributesList.remove(i); // Remove the modifier
                removed = true;
                break; // Exit after removing the first matching modifier
            }
        }

        if (removed) {
            // Update the ItemStack's NBT
            if (attributesList.isEmpty()) {
                pStack.getOrCreateTag().remove("AttributeModifiers"); // Remove the list if empty
            } else {
                pStack.getOrCreateTag().put("AttributeModifiers", attributesList);
            }
        }

    }

    public static void replaceOrAddModifier(ItemStack pStack, Attribute pAttribute, UUID id, double newAmount, @javax.annotation.Nullable EquipmentSlot pSlot, AttributeModifier.Operation operation) {
        // Access the existing attribute modifiers
        ListTag attributesList = pStack.getOrCreateTag().getList("AttributeModifiers", 10);
        boolean modifierExists = false;

        // Create a new modifier
        AttributeModifier newModifier = new AttributeModifier(id, "Tool modifier", newAmount, operation);
        CompoundTag newCompoundTag = newModifier.save();
        newCompoundTag.putString("AttributeName", ForgeRegistries.ATTRIBUTES.getKey(pAttribute).toString());
        if (pSlot != null) {
            newCompoundTag.putString("Slot", pSlot.getName());
        }

        // Iterate through the existing modifiers to check for replacements
        for (int i = 0; i < attributesList.size(); i++) {
            CompoundTag existingCompoundTag = attributesList.getCompound(i);
            UUID existingUUID = existingCompoundTag.getUUID("UUID");

            if (existingUUID.equals(id)) {
                // Replace existing modifier
                attributesList.set(i, newCompoundTag);
                modifierExists = true;
                break;
            }
        }

        if (!modifierExists) {
            attributesList.add(newCompoundTag);
        }

        pStack.getOrCreateTag().put("AttributeModifiers", attributesList);
    }

    public static String ATTACK_DAMAGE_BONUS = "AttackDamageBonus";
    public static void setAttackDamageBonus(ItemStack stack, float bonus) {
        stack.getOrCreateTag().putFloat(ATTACK_DAMAGE_BONUS, bonus);
    }

    public static String ATTACK_SPEED_BONUS = "AttackSpeedBonus";
    public static void setAttackSpeedBonus(ItemStack stack, float speed) {
        stack.getOrCreateTag().putFloat(ATTACK_SPEED_BONUS, speed);
    }
}
