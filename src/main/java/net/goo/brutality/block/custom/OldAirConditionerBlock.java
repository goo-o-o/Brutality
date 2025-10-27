package net.goo.brutality.block.custom;

import net.goo.brutality.block.HorizontalDirectionalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class OldAirConditionerBlock extends HorizontalDirectionalBlock {
    public OldAirConditionerBlock(Properties pProperties) {
        super(pProperties);
    }

    private final VoxelShape SOUTH_AABB = Shapes.or(
            Block.box(-5.5, 0, 0, 16 + 5.5, 2, 17),
            Block.box(-3, 2, 1, 16 + 3, 20, 16),
            Block.box(-5.5, 2, 0, -3.5, 27, 2),
            Block.box(16 + 5.5 - 2, 2, 0, 16 + 5.5, 27, 2)
    );
    private final VoxelShape NORTH_AABB = Shapes.or(
            Block.box(-5.5, 0, -1, 16 + 5.5, 2, 16),
            Block.box(-3, 2, 0, 16 + 3, 20, 15),
            Block.box(-5.5, 2, 14, -3.5, 27, 16),
            Block.box(16 + 5.5 - 2, 2, 14, 16 + 5.5, 27, 16)
    );

    private final VoxelShape WEST_AABB = Shapes.or(
            Block.box(-1 , 0, -5.5, 16, 2, 16 + 5.5),
            Block.box(0, 2, -3, 15, 20, 16 + 3),
            Block.box(14, 2, -5.5, 16, 27, -3.5),
            Block.box(14, 2,16 + 5.5 - 2, 16, 27, 16 + 5.5)
    );
    private final VoxelShape EAST_AABB = Shapes.or(
            Block.box(0, 0, -5.5, 17, 2, 16 + 5.5),
            Block.box(1, 2, -3, 16, 20, 16 + 3),
            Block.box(0, 2, -5.5, 2, 27, -3.5),
            Block.box(0, 2, 16 + 5.5 - 2, 2, 27, 16 + 5.5)
    );



    @Override
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
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
