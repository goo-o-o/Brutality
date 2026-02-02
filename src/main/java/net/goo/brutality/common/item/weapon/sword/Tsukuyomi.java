package net.goo.brutality.common.item.weapon.sword;

import net.goo.brutality.common.item.base.BrutalitySwordItem;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;

import java.util.List;

public class Tsukuyomi extends BrutalitySwordItem {
    public Tsukuyomi(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    @Override
    public float hurtEnemyModifiable(Player attacker, LivingEntity victim, ItemStack weapon, DamageSource source, float amount) {
        if (attacker.level() instanceof ServerLevel serverLevel)
            serverLevel.sendParticles(BrutalityParticles.YANG_PARTICLE.get(), victim.getX(), victim.getY(0.5), victim.getZ(), 10, 1, 1, 1, 0.15);

        return victim.hasEffect(MobEffects.GLOWING) ? amount * 2 : amount;
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pTarget.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100, 0));
        pTarget.removeEffect(MobEffects.GLOWING);
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
