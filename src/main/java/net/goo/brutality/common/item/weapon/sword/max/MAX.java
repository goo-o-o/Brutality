package net.goo.brutality.common.item.weapon.sword.max;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.player_animation.AnimationHelper;
import net.goo.brutality.common.item.weapon.RotatingAttackWeapon;
import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.common.network.serverbound.ServerboundStartPlayerAnimationPacket;
import net.goo.brutality.common.network.serverbound.ServerboundStopPlayerAnimationPacket;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.ColorUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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

    public MAX(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, float secondAttackDamage, int lightningQuota, Rarity rarity) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, secondAttackDamage, lightningQuota, rarity, List.of());
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

//    @Override
//    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
//        consumer.accept(new IClientItemExtensions() {
//            @Override
//            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
//                if (!itemStack.isEmpty() && entityLiving.isUsingItem() && entityLiving.getUseItem() == itemStack) {
//                    return BrutalityPoseHandler.BRUTALITY_SPIN_POSE;
//                }
//                return HumanoidModel.ArmPose.EMPTY;
//            }
//
//            @Override
//            public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
//                BrutalityPoseHandler.getPose(itemInHand.getItem()).applyItem(player, itemInHand, arm, poseStack);
//                return false; // Return true only if you want to skip all vanilla transforms
//            }
//        });
//    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.CUSTOM;
    }

    private static final ResourceLocation SPIN_ANIM = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "pose_two_handed_heavy");

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pUsedHand != InteractionHand.MAIN_HAND)
            return InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));
        pPlayer.startUsingItem(pUsedHand);

        AnimationHelper.playAnimation(pPlayer, SPIN_ANIM, false, 1);
        PacketHandler.sendToServer(new ServerboundStartPlayerAnimationPacket(pPlayer.getUUID(), SPIN_ANIM, false, 1));

        return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        pLivingEntity.walkAnimation.setSpeed(0);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {

        if (pLivingEntity instanceof Player player) {
            AnimationHelper.stopAnimation(player, 5);
            PacketHandler.sendToServer(new ServerboundStopPlayerAnimationPacket(player.getUUID(),5));
        }
        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
    }

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

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }


}
