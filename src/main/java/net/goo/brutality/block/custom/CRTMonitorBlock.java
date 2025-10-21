package net.goo.brutality.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class CRTMonitorBlock extends MonitorBlock{
    public CRTMonitorBlock(Properties pProperties) {
        super(pProperties);
    }

    public static final VoxelShape SOUTH_AABB = Shapes.or(
            Block.box(0, 0, 0, 16, 4, 14),
            Block.box(4, 4, 3, 12, 7, 12),
            Block.box(1.5, 6, 7, 14.5, 17, 14),
            Block.box(3, 7, 0, 13, 13, 8)
    );

    public static final VoxelShape WEST_AABB = Shapes.or(
            Block.box(2, 0, 0, 16, 4, 16),
            Block.box(4, 4, 4, 13, 7, 12),
            Block.box(2, 6, 1.5, 9, 17, 14.5),
            Block.box(8, 7, 3, 16, 13, 13)
    );

    public static final VoxelShape NORTH_AABB = Shapes.or(
            Block.box(0, 0, 2, 16, 4, 16),
            Block.box(4, 4, 4, 12, 7, 13),
            Block.box(1.5, 6, 2, 14.5, 17, 9),
            Block.box(3, 7, 8, 13, 13, 16)
    );

    public static final VoxelShape EAST_AABB = Shapes.or(
            Block.box(0, 0, 0, 14, 4, 16),
            Block.box(3, 4, 4, 12, 7, 12),
            Block.box(7, 6, 1.5, 14, 17, 14.5),
            Block.box(0, 7, 3, 8, 13, 13)
    );

    @Override
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction direction = pState.getValue(FACING);
        VoxelShape shape;
        switch (direction) {
            case SOUTH -> shape = SOUTH_AABB;
            case EAST -> shape = EAST_AABB;
            case WEST -> shape = WEST_AABB;
            default -> shape = NORTH_AABB;
        }
        return shape;
    }
}
