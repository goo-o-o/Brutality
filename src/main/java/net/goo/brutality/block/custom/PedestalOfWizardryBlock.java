package net.goo.brutality.block.custom;

import net.goo.brutality.block.block_entity.PedestalOfWizardryBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class PedestalOfWizardryBlock extends BaseEntityBlock {
    public PedestalOfWizardryBlock(Properties pProperties) {
        super(pProperties);
    }

    protected static final VoxelShape SHAPE = Shapes.or(
            Block.box(4, 0, 4, 12, 1, 12),
            Block.box(6, 1, 6, 10, 14, 10),
            Block.box(2, 14, 2, 14, 16, 14)
    );


    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity be = pLevel.getBlockEntity(pPos);
        if (!(be instanceof PedestalOfWizardryBlockEntity pedestal)) {
            return InteractionResult.PASS;
        }

        ItemStack itemInHand = pPlayer.getItemInHand(pHand);
        ItemStack storedItem = pedestal.getStoredItem();

        // CASE 1: Pedestal is empty, player holding something -> Place item
        if (storedItem.isEmpty() && !itemInHand.isEmpty()) {
            if (!pLevel.isClientSide) {
                // Take 1 from player, put 1 in pedestal
                pedestal.setStoredItem(itemInHand.split(1));
                pLevel.playSound(null, pPos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        }

        // CASE 2: Pedestal has an item -> Take it out
        else if (!storedItem.isEmpty()) {
            if (!pLevel.isClientSide) {
                // Give item to player or drop it
                if (!pPlayer.addItem(storedItem)) {
                    pPlayer.drop(storedItem, false);
                }
                pLevel.playSound(null, pPos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            pedestal.setStoredItem(ItemStack.EMPTY);
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        }

        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (be instanceof PedestalOfWizardryBlockEntity pedestal) {
                Containers.dropItemStack(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), pedestal.getStoredItem());
            }
            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new PedestalOfWizardryBlockEntity(pPos, pState);
    }


    @Override
    public @NotNull RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
}