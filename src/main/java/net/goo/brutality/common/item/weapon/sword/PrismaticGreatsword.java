package net.goo.brutality.common.item.weapon.sword;

import net.goo.brutality.common.item.base.BrutalitySwordItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;

import java.util.List;

public class PrismaticGreatsword extends BrutalitySwordItem {
    public PrismaticGreatsword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker.hasEffect(TerramityModMobEffects.ABILITY_COOLDOWN.get())) {
            MobEffectInstance mobEffectInstance = pAttacker.getEffect(TerramityModMobEffects.ABILITY_COOLDOWN.get());
            pAttacker.removeEffect(TerramityModMobEffects.ABILITY_COOLDOWN.get());
            pAttacker.addEffect(new MobEffectInstance(mobEffectInstance.getEffect(), mobEffectInstance.getDuration() - 20,
                    mobEffectInstance.getAmplifier(), mobEffectInstance.isAmbient(), mobEffectInstance.isVisible(),
                    mobEffectInstance.showIcon()));
        }

        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
