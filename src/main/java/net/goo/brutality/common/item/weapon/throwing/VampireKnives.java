package net.goo.brutality.common.item.weapon.throwing;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.base.BrutalityThrowingItem;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class VampireKnives extends BrutalityThrowingItem {
    public static int ATTACK_SPEED = 5;


    UUID OMNIVAMP_UUID = UUID.fromString("7580ebde-96cf-11f0-a1ff-325096b39f47");

    public VampireKnives(int pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents, Supplier<? extends EntityType<? extends Projectile>> entityTypeSupplier) {
        super(pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents, entityTypeSupplier);
    }


    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            Multimap<Attribute, AttributeModifier> attributeModifierMultimap = super.getAttributeModifiers(slot, stack);
            builder.putAll(attributeModifierMultimap);
            builder.put(BrutalityAttributes.OMNIVAMP.get(), new AttributeModifier(OMNIVAMP_UUID, "Omnivamp Buff", 0.075F, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }


    @Override
    public float getThrowVelocity(Player player) {
        return (float) Math.min(initialThrowVelocity, player.getAttributeValue(Attributes.ATTACK_SPEED));
    }

    @Override
    public void handleThrowPacket(ItemStack stack, Player player) {
        Level level = player.level();

        int quantity = 4;
        quantity += player.getRandom().nextFloat() < 0.5F ? 1 : 0;
        quantity += player.getRandom().nextFloat() < 0.25F ? 1 : 0;
        quantity += player.getRandom().nextFloat() < 0.125F ? 1 : 0;
        quantity += player.getRandom().nextFloat() < 0.0625F ? 1 : 0;

        float gap = 7.5F;
        for (int i = 0; i < quantity; i++) {
            float angleOffset = (i - (quantity - 1) / 2f) * gap; // Center the arc
            angleOffset += (player.getRandom().nextFloat() - 0.5F) * 5F;

            Projectile projectile = entityTypeSupplier.get().create(level);
            if (projectile != null) {
                projectile.setPos(player.getEyePosition());
                projectile.shootFromRotation(player, player.getXRot(), player.getYRot() + angleOffset, 0, getThrowVelocity(player), throwInaccuracy);
                projectile.setOwner(player);

                handleSealType(projectile, stack);


                level.addFreshEntity(projectile);
            }
        }
    }

}
