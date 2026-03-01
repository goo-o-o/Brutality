package net.goo.brutality.common.block.custom;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.gui.screen.TableOfWizardryScreen;
import net.goo.brutality.client.gui.screen.table_of_wizardry.TableOfWizardryBookSection;
import net.goo.brutality.common.block.block_entity.TableOfWizardryBlockEntity;
import net.goo.brutality.common.item.base.BrutalityMagicItem;
import net.goo.brutality.common.registry.BrutalityBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class TableOfWizardryBlock extends BaseEntityBlock {
    protected static final VoxelShape SHAPE = Shapes.or(
            Block.box(2, 0, 2, 14, 3, 14),
            Block.box(4, 3, 4, 12, 14, 12),
            Block.box(0, 14, 0, 16, 16, 16)
    );

    public TableOfWizardryBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    public boolean useShapeForLightOcclusion(BlockState pState) {
        return true;
    }

    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof TableOfWizardryBlockEntity wizardTable) {
                // Drop the crafting item safely
                if (wizardTable.craftingItem != null && !wizardTable.craftingItem.isEmpty()) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), wizardTable.craftingItem);
                }
            }
            level.removeBlockEntity(pos);
        }
    }

    public @NotNull RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TableOfWizardryBlockEntity(pPos, pState);
    }

    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return false;
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide) {
            return createTickerHelper(pBlockEntityType, BrutalityBlockEntities.TABLE_OF_WIZARDRY_BLOCK_ENTITY.get(),
                    TableOfWizardryBlockEntity.Tickers::clientTick);
        }
        return createTickerHelper(pBlockEntityType, BrutalityBlockEntities.TABLE_OF_WIZARDRY_BLOCK_ENTITY.get(),
                TableOfWizardryBlockEntity.Tickers::serverTick);
    }


    @Override
    public @NotNull InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.getBlockEntity(pPos) instanceof TableOfWizardryBlockEntity blockEntity) {
            if (!blockEntity.isCrafting) {
                ItemStack stack = pPlayer.getItemInHand(pHand);
                boolean expunge = (blockEntity.currentSection == TableOfWizardryBookSection.EXPUNGE && stack.getItem() instanceof BrutalityMagicItem);
                if (pPlayer.isShiftKeyDown() || expunge) {
                    if (pPlayer.level().isClientSide()) {
                        if (expunge) {
                            blockEntity.craftingItem = stack;
                        }
                        openWizardScreen(blockEntity);

                    }
                } else
                    blockEntity.tryStartCrafting(pPlayer, pPlayer.getItemInHand(pHand));
            }
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }

    @OnlyIn(Dist.CLIENT)
    private void openWizardScreen(TableOfWizardryBlockEntity blockEntity) {
        Minecraft.getInstance().setScreen(new TableOfWizardryScreen(Component.translatable("block." + Brutality.MOD_ID + ".table_of_wizardry"), blockEntity));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @org.jetbrains.annotations.Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.translatable("block.brutality.table_of_wizardry.description.1"));
    }

    public void setPlacedBy(@NotNull Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        if (pStack.hasCustomHoverName()) {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof TableOfWizardryBlockEntity tableOfWizardryBlockEntity) {
                tableOfWizardryBlockEntity.setCustomName(pStack.getHoverName());
            }
        }

    }

}
