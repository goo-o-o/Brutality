package net.goo.armament.item.custom;

import net.goo.armament.util.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.EnumSet;

public class ShadowstepSwordItem extends SwordItem implements GeoItem {
    AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public ShadowstepSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        Entity entity = ModUtils.getEntityPlayerLookingAt(pPlayer, 35);

        if (!pLevel.isClientSide && entity != null) {
            float distance = 2.0F; // Distance behind the entity
            Vec3 entityPos = entity.position(); // Get the entity’s position
            float entityYaw = entity.getYRot(); // Yaw of the entity

            double offsetX = -distance * Math.sin(Math.toRadians(entityYaw));
            double offsetZ = distance * Math.cos(Math.toRadians(entityYaw));


            Vec3 newPos = entityPos.subtract(offsetX, 0, offsetZ);
            BlockPos targetPos = new BlockPos(new Vec3i(((int) newPos.x), ((int) newPos.y), ((int) newPos.z)));

            if (pLevel.getBlockState(targetPos.above()).isAir() && pLevel.getBlockState(targetPos.below()).isSolid()) {
                // Teleport the player to the calculated position
                pPlayer.teleportTo(pLevel.getServer().getLevel(pPlayer.level().dimension()),
                        newPos.x, newPos.y, newPos.z,
                        EnumSet.allOf(RelativeMovement.class), entityYaw, pPlayer.getXRot());
            } else {
                // Notify player if the teleport location isn't valid
                pPlayer.displayClientMessage(Component.translatable("item.armament.shadowstep.invalid"), true);
            }

        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

}
