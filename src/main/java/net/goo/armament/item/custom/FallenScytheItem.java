package net.goo.armament.item.custom;

import com.google.common.collect.Multimap;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.base.ArmaScytheItem;
import net.goo.armament.util.ModResources;
import net.goo.armament.util.ModUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.UUID;

public class FallenScytheItem extends ArmaScytheItem {
    public static String SOULS_HARVESTED = "souls_harvested";
    public int soulsHarvested;

    public FallenScytheItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount, Multimap<Attribute, AttributeModifier> attributeModifiers) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, category, rarity, abilityCount);
        this.attributeModifiers = attributeModifiers;
        this.colors = ModResources.SUPERNOVA_COLORS;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }


    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        // Check if the target is dead or dying
        if (pTarget.isDeadOrDying()) {
            // Retrieve and update the souls harvested count
            soulsHarvested = pStack.getOrCreateTag().getInt(SOULS_HARVESTED);
            pStack.getOrCreateTag().putInt(SOULS_HARVESTED, (soulsHarvested + 1));

            if (pTarget.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.SOUL, pTarget.getX(), pTarget.getY(), pTarget.getZ(), 2, 0.25, 0.25, 0.25, 0);
                serverLevel.playSound(pTarget, pTarget.getOnPos(), SoundEvents.SOUL_ESCAPE, SoundSource.HOSTILE, 1F, ModUtils.nextFloatBetweenInclusive(serverLevel.random, 0.65F, 1F));
            }
        }

        // Calculate new attack damage
        double newAttackDamage = Math.min((soulsHarvested * 0.05 + 7), 15);

        // Get the current attributes and replace the modifier if it exists
//
//        this.attributeModifiers = ImmutableMultimap.<Attribute, AttributeModifier>builder().build();
//
//        this.attributeModifiers = ImmutableMultimap.<Attribute, AttributeModifier>builder()
//                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 1, AttributeModifier.Operation.ADDITION))
//                .build();


        replaceOrAddModifier(pStack, Attributes.ATTACK_DAMAGE, BASE_ATTACK_DAMAGE_UUID, newAttackDamage, EquipmentSlot.MAINHAND);

        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    private void replaceOrAddModifier(ItemStack pStack, Attribute pAttribute, UUID id, double newAmount, @javax.annotation.Nullable EquipmentSlot pSlot) {
        // Access the existing attribute modifiers
        ListTag attributesList = pStack.getOrCreateTag().getList("AttributeModifiers", 10);
        boolean modifierExists = false;

        // Create a new modifier
        AttributeModifier newModifier = new AttributeModifier(id, "Custom Attack Damage", newAmount, AttributeModifier.Operation.ADDITION);
        CompoundTag newCompoundTag = newModifier.save();
        newCompoundTag.putString("AttributeName", BuiltInRegistries.ATTRIBUTE.getKey(pAttribute).toString());
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

        // If the modifier didn't exist, add it to the list
        if (!modifierExists) {
            attributesList.add(newCompoundTag);
        }

        // Save the updated list back into the ItemStack
        pStack.getOrCreateTag().put("AttributeModifiers", attributesList);
    }
}

