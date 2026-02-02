package net.goo.brutality.common.item.weapon.generic;

import net.goo.brutality.common.item.base.BrutalityGenericItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;

public class DoomfistGauntletItem extends BrutalityGenericItem {
    private static final String PUNCHING = "isPunching";
    private static int clampedTime;

    public DoomfistGauntletItem(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

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
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        if (!pLevel.isClientSide) {
            if (pLivingEntity instanceof ServerPlayer player) {
                pTimeCharged = 72000 - pTimeCharged;


                clampedTime = Math.min(pTimeCharged, 32);
                float movementScale = clampedTime * 0.15F;

                player.addDeltaMovement(getPunchDirection(player).scale(movementScale));
                player.connection.send(new ClientboundSetEntityMotionPacket(player));
                pStack.getOrCreateTag().putBoolean(PUNCHING, true);
                player.getCooldowns().addCooldown(pStack.getItem(), 80);

            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack pStack = pPlayer.getItemInHand(pUsedHand);
        pStack.getOrCreateTag().putBoolean(PUNCHING, false);
        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.consume(pStack);
    }


    int tickCount = 0;
    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);

        if (pIsSelected && !pLevel.isClientSide && pEntity instanceof ServerPlayer player) {

            if (pStack.getOrCreateTag().getBoolean(PUNCHING)) {
                AABB boundingBox = new AABB(
                        player.getX() - 1f, player.getY() - 0.75f, player.getZ(),
                        player.getX() + 1f, player.getY() + 0.75f, player.getZ() + 1f);

                List<LivingEntity> nearbyEntities = pLevel.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, ((LivingEntity) pEntity), boundingBox);

                if (!nearbyEntities.isEmpty()) {
                    LivingEntity firstTarget = nearbyEntities.get(0);
                    List<LivingEntity> targetList = pLevel.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, firstTarget, firstTarget.getBoundingBox().inflate(2));
                    targetList.add(firstTarget);

                    for (LivingEntity target : targetList) {
                        float damageDone = (float) (clampedTime / 2.5);
                        target.hurt(target.damageSources().flyIntoWall(), damageDone);


                        target.setDeltaMovement(target.getDeltaMovement().add(getPunchDirection(player).scale((double) clampedTime / 5)));
                        player.lerpMotion(getPunchDirection(player).x * 0.05,0,getPunchDirection(player).z * 0.05);
                        player.connection.send(new ClientboundSetEntityMotionPacket(player));
                        pStack.getOrCreateTag().putBoolean(PUNCHING, false);
                    }
                }

                tickCount++;
                if (tickCount > 20) {
                    pStack.getOrCreateTag().putBoolean(PUNCHING, false);
                    tickCount = 0;
                }

            }

        }
    }

    public Vec3 getPunchDirection(ServerPlayer player) {
        float yaw = player.getYRot();

        double radians = Math.toRadians(yaw);
        float horizontalMovementX = (float) -Math.sin(radians);
        float horizontalMovementZ = (float) Math.cos(radians);
        Vec3 punchDirection = new Vec3(horizontalMovementX, 0.0, horizontalMovementZ);
        return punchDirection;
    }

}


