package net.goo.brutality.common.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class ImportantDocumentsBlock extends Block {
    private static final int MAX_PAPER = 16;
    public static final IntegerProperty PAPERS = IntegerProperty.create("papers", 1, 16);
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    public ImportantDocumentsBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(PAPERS, 1).setValue(AXIS, Direction.Axis.X));
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return switch (direction) {
            case CLOCKWISE_90, COUNTERCLOCKWISE_90 ->
                    state.getValue(AXIS) == Direction.Axis.X ? state.setValue(AXIS, Direction.Axis.Z) : state.setValue(AXIS, Direction.Axis.X);
            default -> state;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AXIS, PAPERS);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        Direction.Axis axis = context.getHorizontalDirection().getAxis(); // X or Z based on player facing
        if (state.is(this)) {
            return state.setValue(PAPERS, Math.min(MAX_PAPER, state.getValue(PAPERS) + 1));
        } else {
            return this.defaultBlockState().setValue(PAPERS, 1).setValue(AXIS, axis);
        }
    }

    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        return !pUseContext.isSecondaryUseActive() && pUseContext.getItemInHand().is(this.asItem()) && pState.getValue(PAPERS) < MAX_PAPER || super.canBeReplaced(pState, pUseContext);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (pState.getValue(AXIS) == Direction.Axis.X) {
            return Block.box(3, 0, 1.5, 13, pState.getValue(PAPERS), 14.5);
        }
        return Block.box(1.5, 0, 3, 14.5, pState.getValue(PAPERS), 13);
    }
}
