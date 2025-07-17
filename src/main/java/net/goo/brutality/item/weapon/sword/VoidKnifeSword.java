package net.goo.brutality.item.weapon.custom;

import net.goo.brutality.client.renderers.item.BrutalityAutoEndPortalRenderer;
import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class VoidKnifeSword extends BrutalitySwordItem {


    public VoidKnifeSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, identifier, rarity, descriptionComponents);
    }

    @Override
    public <R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeo(consumer, BrutalityAutoEndPortalRenderer.class);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(1, pAttacker, (attacker) -> {
            attacker.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });

        if (pTarget.hasEffect(BrutalityModMobEffects.SCORED.get())) {
            int amplifier = Objects.requireNonNull(pTarget.getEffect(BrutalityModMobEffects.SCORED.get())).getAmplifier();

            if (amplifier > 3) return true;

            pTarget.addEffect(new MobEffectInstance(BrutalityModMobEffects.SCORED.get(), 60, amplifier + 1));
        }
        pTarget.addEffect(new MobEffectInstance(BrutalityModMobEffects.SCORED.get(), 60, 0));
        pTarget.addEffect(new MobEffectInstance(BrutalityModMobEffects.PULVERIZED.get(), 3, 2));

        return true;
    }
}
