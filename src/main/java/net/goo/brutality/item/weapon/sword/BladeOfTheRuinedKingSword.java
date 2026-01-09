package net.goo.brutality.item.weapon.sword;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.entity.projectile.generic.SpectralMawEntity;
import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.registry.BrutalityModAttributes;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class BladeOfTheRuinedKingSword extends BrutalitySwordItem {


    public BladeOfTheRuinedKingSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }


    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pTarget.invulnerableTime = 0;

        ModUtils.modifyEffect(pTarget, BrutalityModMobEffects.RUINED.get(), new ModUtils.ModValue(120, true), new ModUtils.ModValue(1, false), 3, e -> e.addEffect(new MobEffectInstance(BrutalityModMobEffects.RUINED.get(), 120, 0)),
                e -> {
                    e.removeEffect(BrutalityModMobEffects.RUINED.get());

                    e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 1));

                    pAttacker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 1));

                });

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
    public @NotNull UseAnim getUseAnimation(ItemStack pStack) {
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

    UUID BORK_LIFESTEAL_UUID = UUID.fromString("b38bb68d-31fd-4647-9fb2-655110b69fcb");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> original = super.getAttributeModifiers(slot, stack);

        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> newAttributes = ImmutableMultimap.builder();
            newAttributes.putAll(original);
            newAttributes.put(BrutalityModAttributes.LIFESTEAL.get(),
                    new AttributeModifier(BORK_LIFESTEAL_UUID, "Lifesteal buff", 0.1, AttributeModifier.Operation.MULTIPLY_BASE));

            return newAttributes.build();
        }

        return super.getAttributeModifiers(slot, stack);
    }

}
