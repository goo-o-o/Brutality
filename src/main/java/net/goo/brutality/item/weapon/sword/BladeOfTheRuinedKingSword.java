package net.goo.brutality.item.weapon.sword;

import net.goo.brutality.entity.projectile.generic.SpectralMawEntity;
import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;

public class BladeOfTheRuinedKingSword extends BrutalitySwordItem {


    public BladeOfTheRuinedKingSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

//    @Override
//    public <T extends Item & BrutalityGeoItem> void configureLayers(BrutalityItemRenderer<T> renderer) {
//        super.configureLayers(renderer);
//        renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer));
//    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pTarget.invulnerableTime = 0;
        MobEffectInstance currentEffect = pTarget.getEffect(BrutalityModMobEffects.RUINED.get());
        int currentAmplifier = currentEffect != null ? currentEffect.getAmplifier() : -1;

        if (currentAmplifier < 2) {
            pTarget.addEffect(new MobEffectInstance(
                    BrutalityModMobEffects.RUINED.get(),
                    120, // 6 seconds
                    currentAmplifier + 1,
                    false, false, true
            ));
        } else {
            pTarget.removeEffect(BrutalityModMobEffects.RUINED.get());
            pTarget.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SLOWDOWN,
                    20,
                    1,
                    false, false, false
            ));
        }
        if (pAttacker instanceof Player player) {
            pTarget.hurt(pAttacker.damageSources().playerAttack(player), pTarget.getHealth() * 0.08F);
        } else {
            pTarget.hurt(pAttacker.damageSources().mobAttack(pAttacker), pTarget.getHealth() * 0.08F);
        }

        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {

        if (!pLevel.isClientSide) {
            SpectralMawEntity spectralMaw = new SpectralMawEntity(BrutalityModEntities.SPECTRAL_MAW_ENTITY.get(), pLivingEntity, pLevel, 72000 - pTimeCharged);
            spectralMaw.shootFromRotation(pLivingEntity, pLivingEntity.getXRot(), pLivingEntity.getYRot(), 0.0F, 1, 1.0F);
            pLevel.addFreshEntity(spectralMaw);
        }



        if (pLivingEntity instanceof Player player) {
            player.getCooldowns().addCooldown(pStack.getItem(), 60);
        }

        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
    }
}
