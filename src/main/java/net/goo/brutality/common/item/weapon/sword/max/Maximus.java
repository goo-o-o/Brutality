package net.goo.brutality.common.item.weapon.sword.max;

import net.goo.brutality.util.lightning.ChainLightningHelper;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;

import java.util.List;

public class Maximus extends Maxima {
    private final int lightningQuota;
    private final float chainLightningDamageRatio;

    public Maximus(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, float secondAttackDamage, int lightningQuota, float chainLightningDamageRatio, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, secondAttackDamage, rarity, descriptionComponents);
        this.lightningQuota = lightningQuota;
        this.chainLightningDamageRatio = chainLightningDamageRatio;
        this.rangeBonus = 3;

    }


    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
            ChainLightningHelper.chainLightning(pAttacker, pStack, pTarget, lightningQuota, 3.5F, (float) pAttacker.getAttributeValue(Attributes.ATTACK_DAMAGE) * chainLightningDamageRatio, 0.1F, 3, ChainLightningHelper.LightningType.MAX);
        }
        // trigger max alpha here and slowly tick down
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
