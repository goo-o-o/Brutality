package net.goo.brutality.common.item.weapon.sword.max;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.base.BrutalitySwordItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;

import java.util.List;

import static net.goo.brutality.common.registry.BrutalityAttributes.BASE_ENTITY_RANGE_UUID;

public class Maxim extends BrutalitySwordItem {
    protected int rangeBonus;

    public Maxim(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
        this.rangeBonus = 0;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 7200;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pLevel.isClientSide()) {
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot != EquipmentSlot.MAINHAND) return super.getAttributeModifiers(slot, stack);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(BASE_ENTITY_RANGE_UUID, "Range Buff", this.rangeBonus, AttributeModifier.Operation.ADDITION));
        builder.putAll(super.getAttributeModifiers(slot, stack));
        return builder.build();
    }


}
