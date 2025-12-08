package net.goo.brutality.item.weapon.axe;

import net.goo.brutality.client.ClientAccess;
import net.goo.brutality.config.BrutalityCommonConfig;
import net.goo.brutality.item.base.BrutalityAxeItem;
import net.goo.brutality.registry.BrutalityDamageTypes;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.goo.brutality.util.phys.OrientedBoundingBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Deathsaw extends BrutalityAxeItem {
    public Deathsaw(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    public static final OrientedBoundingBox HITBOX = new OrientedBoundingBox(Vec3.ZERO, new Vec3(1 / 8F, 0.75, 2.5F).scale(0.5F), 0, 0, 0);


    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        ModUtils.setTextureIdx(stack, 1);
        pPlayer.startUsingItem(pUsedHand);
        ClientAccess.startDeathsawSound(pPlayer);
        return InteractionResultHolder.pass(stack);
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
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (pLivingEntity instanceof Player player) {
            OrientedBoundingBox.TargetResult<LivingEntity> targets = OrientedBoundingBox.findAttackTargetResult(player, LivingEntity.class, HITBOX, new Vec3(0, 0, 1), true);
            targets.entities.forEach(e -> e.hurt(BrutalityDamageTypes.deathsaw(player), BrutalityCommonConfig.DEATHSAW_TICK_DAMAGE.get()));

            Direction lookDir = pLivingEntity.getDirection();
            BlockPos pos = pLivingEntity.blockPosition().relative(lookDir, 1);
            BlockState blockState = pLevel.getBlockState(pos);
            if (!blockState.isAir()) {
                if (pLivingEntity.getXRot() < -80) {
                    if (pLivingEntity.horizontalCollision) {
                        Vec3 deltaMovement = pLivingEntity.getDeltaMovement();
                        deltaMovement = new Vec3(deltaMovement.x, 0.2D * BrutalityCommonConfig.DEATHSAW_WALL_CLIMB_SPEED.get(), deltaMovement.z);
                        pLivingEntity.setDeltaMovement(deltaMovement);
                    }

                }
            }
        }
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        ModUtils.removeTextureIdx(item);

        return super.onDroppedByPlayer(item, player);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        ModUtils.removeTextureIdx(pStack);
        if (pLivingEntity instanceof Player player) player.getCooldowns().addCooldown(pStack.getItem(), 10);
        ClientAccess.stopDeathsawSound(pLivingEntity);
        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pTarget.addEffect(new MobEffectInstance(BrutalityModMobEffects.PULVERIZED.get(), 5, 1));
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
