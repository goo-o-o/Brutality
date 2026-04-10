package net.goo.brutality.common.item.weapon.axe;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.client.ClientProxy;
import net.goo.brutality.common.item.ItemEquipUnequipTriggerable;
import net.goo.brutality.common.item.base.BrutalityAxeItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.ForgeMod;

import java.util.List;
import java.util.UUID;

public class OldGpu extends BrutalityAxeItem implements ItemEquipUnequipTriggerable {

    public OldGpu(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    UUID OLD_GPU_RANGE_UUID = UUID.fromString("ad80005a-a3dc-4f76-86f1-6ce6efc8ae24");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);

        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(modifiers);
            builder.put(
                    ForgeMod.ENTITY_REACH.get(),
                    new AttributeModifier(
                            OLD_GPU_RANGE_UUID,
                            "Reach bonus",
                            2,
                            AttributeModifier.Operation.ADDITION
                    )
            );

            return builder.build();
        }
        return modifiers;
    }

    @Override
    public void onEnterMainHand(LivingEntity livingEntity, ItemStack stack) {
        if (livingEntity.level().isClientSide) {
            ClientProxy.loadBitShader();
        }
    }

    @Override
    public void onLeaveMainHand(LivingEntity livingEntity, ItemStack stack) {
        if (livingEntity.level().isClientSide) {
            ClientProxy.stopAllShaders();
        }
    }


}
