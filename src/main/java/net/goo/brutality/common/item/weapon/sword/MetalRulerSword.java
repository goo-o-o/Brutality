package net.goo.brutality.common.item.weapon.sword;

import net.goo.brutality.common.item.base.BrutalitySwordItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MetalRulerSword extends BrutalitySwordItem {


    public MetalRulerSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.enchant(Enchantments.SHARPNESS,5);
        return stack;
    }

    @Override
    public float hurtEnemyModifiable(Player attacker, LivingEntity victim, ItemStack weapon, DamageSource source, float amount) {
        return amount * (victim.getBbHeight() * 0.75F);
    }
}
