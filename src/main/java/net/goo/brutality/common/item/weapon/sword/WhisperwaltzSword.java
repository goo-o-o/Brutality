package net.goo.brutality.common.item.weapon.sword;

import net.goo.brutality.common.item.base.BrutalitySwordItem;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;

public class WhisperwaltzSword extends BrutalitySwordItem {

    public WhisperwaltzSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        MobEffect pulverized = BrutalityEffects.PULVERIZED.get();
        pTarget.invulnerableTime = 0;
        pTarget.addEffect(new MobEffectInstance(pulverized, 2, 1));
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
