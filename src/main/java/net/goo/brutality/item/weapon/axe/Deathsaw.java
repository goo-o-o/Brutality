package net.goo.brutality.item.weapon.axe;

import net.bettercombat.logic.TargetHelper;
import net.goo.brutality.item.base.BrutalityAxeItem;
import net.goo.brutality.registry.BrutalityDamageTypes;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.goo.brutality.util.phys.OrientedBoundingBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Deathsaw extends BrutalityAxeItem {
    public Deathsaw(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        ModUtils.setTextureIdx(stack, 1);
        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.pass(stack);
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
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (pLivingEntity instanceof Player player) {
            Vec3 origin = OrientedBoundingBox.getInitialTracingPoint(player);
            Vec3 hitbox = new Vec3(1, 1, 5);
            OrientedBoundingBox oob = new OrientedBoundingBox(origin, hitbox, player.getViewYRot(0), player.getViewYRot(0));
            oob.updateVertex();

            List<Entity> initial = pLevel.getEntities(pLivingEntity, pLivingEntity.getBoundingBox().inflate(5), Entity::isAttackable);
            oob.filterEntities(initial).forEach(e -> e.hurt(BrutalityDamageTypes.deathsaw(player), 1));

            Direction lookDir = pLivingEntity.getDirection();
            BlockPos pos = pLivingEntity.blockPosition().relative(lookDir, 1);
            BlockState blockState = pLevel.getBlockState(pos);
//            pLivingEntity.sendSystemMessage(Component.literal("POS: " + pos + " | Block: " + blockState.getBlock()));
//            pLivingEntity.sendSystemMessage(Component.literal("Pitch: " + pLivingEntity.getXRot()));
            if (!blockState.isAir()) {
                if (pLivingEntity.getXRot() < -80) {
                    Vec3 deltaMovement = pLivingEntity.getDeltaMovement();
                    if (pLivingEntity.horizontalCollision) {
                        deltaMovement = new Vec3(deltaMovement.x, 0.2D, deltaMovement.z);
                    }
                    pLivingEntity.setDeltaMovement(deltaMovement.multiply(1, 0.8F, 1));

                }
            }
        }
    }


    public List<Entity> filter(OrientedBoundingBox obb, List<Entity> entities) {
        return entities.stream()
                .filter(entity -> obb.intersects(entity.getBoundingBox())
                        || obb.contains(entity.getPosition(0).add(0, entity.getBbHeight() / 2F, 0))
                )
                .collect(Collectors.toList());
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        ModUtils.removeTextureIdx(pStack);
        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pTarget.addEffect(new MobEffectInstance(BrutalityModMobEffects.PULVERIZED.get(), 5, 1));
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
