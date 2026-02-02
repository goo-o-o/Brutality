package net.goo.brutality.common.item.weapon.staff;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.base.BrutalityStaffItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.UUID;

public class ChopstickStaff extends BrutalityStaffItem {
    public ChopstickStaff(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }


    UUID CHOPSTICK_RANGE_UUID = UUID.fromString("ca56890e-c15e-4904-b84f-8b394ba3bddb");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);

        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(modifiers);
            builder.put(
                    ForgeMod.ENTITY_REACH.get(),
                    new AttributeModifier(
                            CHOPSTICK_RANGE_UUID,
                            "Reach bonus",
                            2,
                            AttributeModifier.Operation.ADDITION
                    )
            );

            return builder.build();
        }
        return modifiers;
    }

//    @Override
//    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
//        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
//
//        if (pEntity instanceof Player player && player.getOffhandItem() == pStack) {
//            if (!player.getMainHandItem().is(pStack.getItem())) {
//                if (player.getInventory().getFreeSlot() != -1) { // Check if theres free slot
//                    int freeIdx = player.getInventory().getFreeSlot();
//                    player.getInventory().setItem(freeIdx, pStack.copyAndClear());
//                } else player.drop(pStack, true);
//            }
//        }
//    }
}
