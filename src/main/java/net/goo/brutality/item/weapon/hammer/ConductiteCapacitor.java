package net.goo.brutality.item.weapon.hammer;

import net.goo.brutality.item.base.BrutalityHammerItem;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class ConductiteCapacitor extends BrutalityHammerItem {


    public ConductiteCapacitor(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, @NotNull LivingEntity pTarget, @NotNull LivingEntity pAttacker) {
        pTarget.addEffect(new MobEffectInstance(TerramityModMobEffects.ELECTRIC_SHOCK_EFFECT.get(), 40));

        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
