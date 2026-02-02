package net.goo.brutality.common.item.weapon.hammer;

import net.goo.brutality.common.item.base.BrutalityHammerItem;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WhiskHammer extends BrutalityHammerItem {


    public WhiskHammer(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, @NotNull LivingEntity pTarget, @NotNull LivingEntity pAttacker) {
        pTarget.addEffect(new MobEffectInstance(BrutalityEffects.MASHED.get(), 200, 0, false, true));

        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
