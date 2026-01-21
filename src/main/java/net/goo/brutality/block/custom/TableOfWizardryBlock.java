package net.goo.brutality.block.custom;

import net.goo.brutality.Brutality;
import net.goo.brutality.block.block_entity.TableOfWizardryBlockEntity;
import net.goo.brutality.gui.screen.TableOfWizardryScreen;
import net.goo.brutality.registry.BrutalityModBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

public class TableOfWizardryBlock extends BaseEntityBlock {
    protected static final VoxelShape SHAPE = Shapes.or(
            Block.box(2, 0, 2, 14, 3, 14),
            Block.box(4, 3, 4, 12,14,12),
            Block.box(0, 14,0, 16,16,16)
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

    public @NotNull RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TableOfWizardryBlockEntity(pPos, pState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? createTickerHelper(pBlockEntityType, BrutalityModBlockEntities.TABLE_OF_WIZARDRY_BLOCK_ENTITY.get(), TableOfWizardryBlockEntity::bookAnimationTick) : null;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            // Open the screen only on the client
            openWizardScreen();
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }

    // Separate method to avoid server-side classloading issues
    @OnlyIn(Dist.CLIENT)
    private void openWizardScreen() {
        Minecraft.getInstance().setScreen(new TableOfWizardryScreen(Component.translatable("block." + Brutality.MOD_ID + ".table_of_wizardry")));
    }


    public void setPlacedBy(@NotNull Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        if (pStack.hasCustomHoverName()) {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof TableOfWizardryBlockEntity tableOfWizardryBlockEntity) {
                tableOfWizardryBlockEntity.setCustomName(pStack.getHoverName());
            }
        }

    }

    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return false;
    }
}
