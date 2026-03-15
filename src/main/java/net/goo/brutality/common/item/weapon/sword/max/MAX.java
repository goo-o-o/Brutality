package net.goo.brutality.common.item.weapon.sword.max;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.weapon.RotatingAttackWeapon;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.goo.brutality.util.ColorUtils;
import net.goo.brutality.util.math.phys.hitboxes.ArcCylindricalBoundingBox;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MAX extends Maximus implements RotatingAttackWeapon {

    public MAX(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, float secondAttackDamage, int lightningQuota, float chainLightningDamageRatio, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, secondAttackDamage, lightningQuota, chainLightningDamageRatio, rarity, descriptionComponents);
        this.rangeBonus = 6;
    }

    @Override
    public float getMaxRotationsPerSecond() {
        return 1.25F;
    }

    @Override
    public int getTicksTillMaxSpeed() {
        return 30;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }


    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.CUSTOM;
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (pLivingEntity instanceof Player player) {
            ArcCylindricalBoundingBox arc = RotatingAttackWeapon.getHitbox(player, 0.5F, 9, pStack, this, BrutalitySounds.HEAVY_WHOOSH.get());
            arc.findEntitiesHit(player, LivingEntity.class).forEach(player::attack);
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pUsedHand != InteractionHand.MAIN_HAND)
            return InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));
        pPlayer.startUsingItem(pUsedHand);

//        AnimationHelper.playAnimation(pPlayer, SPIN_ANIM, false, 1);
//        PacketHandler.sendToServer(new ServerboundStartPlayerAnimationPacket(pPlayer.getUUID(), SPIN_ANIM, false, 1));

        return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand));
    }


//    @Override
//    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
//
//        if (pLivingEntity instanceof Player player) {
//            AnimationHelper.stopAnimation(player, 5);
//            PacketHandler.sendToServer(new ServerboundStopPlayerAnimationPacket(player.getUUID(),5));
//        }
//        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
//    }

    public static float getDamageBonusFromHealth(Player player, ItemStack stack) {
        float missingHealth = player.getMaxHealth() - player.getHealth();
        if (stack.is(BrutalityItems.MAXIM.get())) {
            return missingHealth * 2;
        } else if (stack.is(BrutalityItems.MAXIMA.get())) {
            return missingHealth * 3;
        } else if (stack.is(BrutalityItems.MAXIMUS.get())) {
            return missingHealth * 4;
        } else if (stack.is(BrutalityItems.MAX.get())) {
            return missingHealth * 5;
        }
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + ".max.lore.1"));
        pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + ".max.lore.2"));
        pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + ".max.lore.3"));
        pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + ".max.lore.4")
                .withStyle(style -> style.withInsertion(ColorUtils.ColorData.MAX.name()).withBold(true)));

        pTooltipComponents.add(Component.empty());
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }


}
