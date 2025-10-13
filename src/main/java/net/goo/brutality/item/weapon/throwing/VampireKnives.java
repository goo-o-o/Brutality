package net.goo.brutality.item.weapon.throwing;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityThrowingItem;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.ServerboundShootFromRotationPacket;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.List;
import java.util.UUID;

public class VampireKnives extends BrutalityThrowingItem {


    public VampireKnives(int pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories.AttackType getAttackType() {
        return BrutalityCategories.AttackType.PIERCE;
    }

    @Override
    public EntityType<? extends Projectile> getThrownEntity() {
        return BrutalityModEntities.VAMPIRE_KNIFE.get();
    }

    UUID OMNIVAMP_UUID = UUID.fromString("7580ebde-96cf-11f0-a1ff-325096b39f47");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            Multimap<Attribute, AttributeModifier> attributeModifierMultimap = super.getAttributeModifiers(slot, stack);
            builder.putAll(attributeModifierMultimap);
            builder.put(ModAttributes.OMNIVAMP.get(), new AttributeModifier(OMNIVAMP_UUID, "Omnivamp Buff", 0.075F, AttributeModifier.Operation.MULTIPLY_BASE));
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }


    @Override
    public float getThrowVelocity(Player player) {
        return (float) Math.min(3, player.getAttributeValue(Attributes.ATTACK_SPEED));
    }

    @Override
    public void throwProjectile(ItemStack stack, Player player) {
        int quantity = 4;
        quantity += player.getRandom().nextFloat() < 0.5F ? 1 : 0;
        quantity += player.getRandom().nextFloat() < 0.25F ? 1 : 0;
        quantity += player.getRandom().nextFloat() < 0.125F ? 1 : 0;
        quantity += player.getRandom().nextFloat() < 0.0625F ? 1 : 0;
        float gap = 7.5F;
        for (int i = 0; i < quantity; i++) {
            float angleOffset = (i - (quantity - 1) / 2f) * gap; // Center the arc
            angleOffset += (player.getRandom().nextFloat() - 0.5F) * 5F;
            PacketHandler.sendToServer(new ServerboundShootFromRotationPacket(stack, this.getThrownEntity(), player.getEyePosition(),
                    player.getXRot(), player.getYRot() + angleOffset, this.getThrowVelocity(player), this.getThrowInaccuracy()));
        }
    }
}
