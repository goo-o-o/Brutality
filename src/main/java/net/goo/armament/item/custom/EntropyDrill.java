package net.goo.armament.item.custom;

import net.goo.armament.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.minecraft.core.BlockPos.withinManhattan;

public class EntropyDrill extends Item implements GeoItem {
    private static final String ACTIVE = "drillActive";
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public EntropyDrill(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack pStack = pPlayer.getItemInHand(pUsedHand);

        if (pLevel.isClientSide) {
            // placeholder (sound, animation)
        }

        if (!pLevel.isClientSide) {
            setActive(pStack, !isActive(pStack));
        }

        return InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));
    }

    int tickCount = 0;
    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        tickCount++;
        if (tickCount % 10 == 0) {
            if (isActive(pStack)) {

                Set<BlockState> blockSet = new HashSet<>();

                BlockPos origin = new BlockPos(pEntity.getBlockX(), pEntity.getBlockY(), pEntity.getBlockZ());
                int radius = 5;

                for (BlockPos pos : withinManhattan(origin, radius, radius, radius)) {
                    blockSet.add(pLevel.getBlockState(pos));
                }

                int uniqueBlocks = blockSet.size();
                float effeciency = (float) uniqueBlocks / 10;
                pEntity.sendSystemMessage(Component.literal("UNIQUE BLOCKS: " + uniqueBlocks));
                pEntity.sendSystemMessage(Component.literal("EFFECIENCY: " + effeciency));
                // Check for nearby blocks
                // Compile into list
                // Combine duplicate entries using set
                // Get length of list
                // Update mining speed based on list
            }
        }

    }

    public boolean isActive(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean(ACTIVE);
    }

    public static void setActive(ItemStack stack, boolean active) {
        stack.getOrCreateTag().putBoolean(ACTIVE, active);
    }

    private PlayState predicate(AnimationState animationState) {
        ItemStack stack = (ItemStack) animationState.getData(DataTickets.ITEMSTACK);
        if (isActive(stack)) {
            animationState.getController().setAnimation(RawAnimation.begin().thenPlayAndHold("active"));
        } else {
            animationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
