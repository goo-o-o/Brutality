package net.goo.brutality.common.item.weapon.sword.max;

import net.goo.brutality.common.item.base.BrutalitySwordItem;
import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;

import java.util.List;

public class Maxima extends BrutalitySwordItem {
    private final float secondAttackDamage;

    public Maxima(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, float secondAttackDamage, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
        this.secondAttackDamage = secondAttackDamage;
    }


    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player && !attacker.level().isClientSide) {
            if (!stack.getOrCreateTag().getBoolean("hit"))
                DelayedTaskScheduler.queueCommonWork(attacker.level(), 3, () -> {
                    if (target.isAlive() && player.getMainHandItem() == stack) {
                        // Set a temporary "hit" tag
                        stack.getOrCreateTag().putBoolean("hit", true);
                        ModUtils.resetAttackCooldown(player);
                        target.invulnerableTime = 0;
                        player.attack(target);

                        // Clean up the tag immediately after
                        stack.getOrCreateTag().remove("hit");
                    }
                });
        }
        return true;
    }

    @Override
    public float hurtEnemyModifiable(Player attacker, LivingEntity victim, ItemStack weapon, float amount) {
        if (weapon.hasTag() && weapon.getTag().getBoolean("ProcessingSecondHit")) {
            return amount * secondAttackDamage;
        }

        return amount;
    }
}
