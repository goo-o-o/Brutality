package net.goo.brutality.common.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ExitSignBlock extends FaceAttachedHorizontalDirectionalBlock {
    public ExitSignBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(getStateDefinition().any().setValue(FACE, AttachFace.WALL).setValue(FACING, Direction.NORTH));
    }

    private static final VoxelShape NORTH_AABB = Block.box(0, 4.5, 14, 16, 4.5 + 7, 16);
    private static final VoxelShape SOUTH_AABB = Block.box(0, 4.5, 0, 16, 4.5 + 7, 2);
    private static final VoxelShape EAST_AABB = Block.box(0, 4.5, 0, 2, 4.5 + 7, 16);
    private static final VoxelShape WEST_AABB = Block.box(14, 4.5, 0, 16, 4.5 + 7, 16);
    private static final VoxelShape DOWN_AABB_Z = Block.box(0, 14, 4.5, 16, 16, 4.5 + 7);
    private static final VoxelShape DOWN_AABB_X = Block.box(4.5, 14, 0, 4.5 + 7, 16, 16);
    private static final VoxelShape UP_AABB_Z = Block.box(0, 0, 4.5, 16, 2, 4.5 + 7);
    private static final VoxelShape UP_AABB_X = Block.box(4.5, 0, 0, 4.5 + 7, 2, 16);

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 5;
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch (pState.getValue(FACE)) {
            case FLOOR -> switch (pState.getValue(FACING).getAxis()) {
                case X -> UP_AABB_X;
                default -> UP_AABB_Z;
            };
            case WALL -> switch (pState.getValue(FACING)) {
                case EAST -> EAST_AABB;
                case WEST -> WEST_AABB;
                case SOUTH -> SOUTH_AABB;
                default -> NORTH_AABB;
            };
            default -> switch (pState.getValue(FACING).getAxis()) {
                case X -> DOWN_AABB_X;
                default -> DOWN_AABB_Z;
            };
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACE, FACING);
    }
}
