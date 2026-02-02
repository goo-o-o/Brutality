package net.goo.brutality.common.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class SmallOfficeLightBlock extends FaceAttachedHorizontalDirectionalBlock {
    public SmallOfficeLightBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(getStateDefinition().any().setValue(FACE, AttachFace.WALL).setValue(FACING, Direction.NORTH));
    }

    private static final VoxelShape NORTH_AABB = Block.box(5, 5, 15, 11, 11, 16);
    private static final VoxelShape SOUTH_AABB = Block.box(5, 5, 0, 11, 11, 1);
    private static final VoxelShape EAST_AABB = Block.box(0, 5, 5, 1, 11, 11);
    private static final VoxelShape WEST_AABB = Block.box(15, 5, 5, 16, 11, 11);
    private static final VoxelShape DOWN_AABB = Block.box(5, 0, 5, 11, 1, 11);
    private static final VoxelShape UP_AABB = Block.box(5, 15, 5, 11, 16, 11);

    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return canAttach(pLevel, pPos, getConnectedDirection(pState).getOpposite());
    }

    public static boolean canAttach(LevelReader pReader, BlockPos pPos, Direction pDirection) {
        BlockPos blockpos = pPos.relative(pDirection);
        return pReader.getBlockState(blockpos).isFaceSturdy(pReader, blockpos, pDirection.getOpposite(), SupportType.CENTER);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 15;
    }

    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch (pState.getValue(FACE)) {
            case CEILING -> UP_AABB;
            case FLOOR -> DOWN_AABB;
            case WALL -> switch (pState.getValue(FACING)) {
                case EAST -> EAST_AABB;
                case WEST -> WEST_AABB;
                case SOUTH -> SOUTH_AABB;
                default -> NORTH_AABB;
            };
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACE, FACING);
    }
}
