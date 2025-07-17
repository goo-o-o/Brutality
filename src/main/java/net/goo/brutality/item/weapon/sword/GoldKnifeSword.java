package net.goo.brutality.item.weapon.custom;

import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;

public class GoldKnifeSword extends BrutalitySwordItem {


    public GoldKnifeSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, identifier, rarity, descriptionComponents);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }



    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(1, pAttacker, (attacker) -> {
            attacker.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });

        if (pTarget.hasEffect(BrutalityModMobEffects.SCORED.get())) {
            int amplifier = pTarget.getEffect(BrutalityModMobEffects.SCORED.get()).getAmplifier();

            if (amplifier > 2) return true;

            pTarget.addEffect(new MobEffectInstance(BrutalityModMobEffects.SCORED.get(), 60, amplifier + 1));
        }
        pTarget.addEffect(new MobEffectInstance(BrutalityModMobEffects.SCORED.get(), 60, 0));

        return true;
    }
}
