package net.goo.brutality.item.weapon.lance;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.base.BrutalityLanceItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.UUID;

public class ChopstickStaff extends BrutalityLanceItem {


    public ChopstickStaff(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
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

    UUID CHOPSTICK_AS_UUID = UUID.fromString("cd9b5958-a78b-4391-af97-826d4379f7f0");
    UUID CHOPSTICK_KB_UUID = UUID.fromString("3f0426a3-2e8a-4d29-b06e-7a470bf28015");

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if (pEntity instanceof Player player) {
            AttributeInstance knockback = player.getAttribute(Attributes.ATTACK_KNOCKBACK);
            AttributeInstance attackSpeed = player.getAttribute(Attributes.ATTACK_SPEED);

            if (pIsSelected) {
                if (player.getMainHandItem() == player.getOffhandItem()) {
                    if (attackSpeed != null)
                        attackSpeed.addTransientModifier(
                                new AttributeModifier(
                                        CHOPSTICK_AS_UUID,
                                        "Temporary AS Bonus",
                                        0.5,
                                        AttributeModifier.Operation.MULTIPLY_BASE
                                )
                        );

                    if (knockback != null)
                        knockback.addTransientModifier(
                                new AttributeModifier(
                                        CHOPSTICK_KB_UUID,
                                        "Temporary KB Bonus",
                                        2,
                                        AttributeModifier.Operation.MULTIPLY_BASE
                                )
                        );
                }
            } else {
                if (attackSpeed != null) attackSpeed.removeModifier(CHOPSTICK_AS_UUID);
                if (knockback != null) knockback.removeModifier(CHOPSTICK_KB_UUID);
            }

        }

    }
}
