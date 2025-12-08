package net.goo.brutality.block.custom;

import net.goo.brutality.block.HorizontalDirectionalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class UrinalBlock extends HorizontalDirectionalBlock {

    public UrinalBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 0.6F;
    }

    public static final VoxelShape SOUTH_AABB = Block.box(3, 0, 0, 13, 25, 5);
    public static final VoxelShape NORTH_AABB = Block.box(3, 0, 11, 13, 25, 16);
    public static final VoxelShape EAST_AABB = Block.box(0, 0, 3, 5, 25, 13);
    public static final VoxelShape WEST_AABB = Block.box(11, 0, 3, 16, 25, 13);


    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction direction = pState.getValue(FACING);
        switch (direction) {
            case SOUTH -> {
                return SOUTH_AABB;
            }
            case EAST -> {
                return EAST_AABB;
            }
            case WEST -> {
                return WEST_AABB;
            }
            default -> {
                return NORTH_AABB;
            }
        }
    }

}
