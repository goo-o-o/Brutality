package net.goo.brutality.common.block.custom;

import net.goo.brutality.common.block.HorizontalDirectionalBlock;
import net.goo.brutality.common.entity.ChairSeatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ToiletBlock extends HorizontalDirectionalBlock {

    public ToiletBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 0.6F;
    }

    public static final VoxelShape SOUTH_AABB = Shapes.or(
            Block.box(4, 0, 0, 12, 4, 11),
            Block.box(6, 4, 0, 10, 8, 2),
            Block.box(2, 4, 2, 14, 8, 15),
            Block.box(2, 8, 5, 14, 10, 16),
            Block.box(2, 17, 0, 14, 19, 6),
            Block.box(2, 8, 0, 14, 17, 5)
    );

    public static final VoxelShape NORTH_AABB = Shapes.or(
            Block.box(4, 0, 5, 12, 4, 16),
            Block.box(6, 4, 14, 10, 8, 16),
            Block.box(2, 4, 1, 14, 8, 14),
            Block.box(2, 8, 0, 14, 10, 11),
            Block.box(2, 17, 10, 14, 19, 16),
            Block.box(2, 8, 11, 14, 17, 16)
    );

    public static final VoxelShape WEST_AABB = Shapes.or(
            Block.box(5, 0, 4, 16, 4, 12),
            Block.box(14, 4, 6, 16, 8, 10),
            Block.box(1, 4, 2, 14, 8, 14),
            Block.box(11, 8, 2, 16, 10, 14),
            Block.box(10, 17, 2, 16, 19, 14),
            Block.box(11, 8, 2, 16, 17, 14)
    );

    public static final VoxelShape EAST_AABB = Shapes.or(
            Block.box(0, 0, 4, 11, 4, 12),
            Block.box(0, 4, 6, 2, 8, 10),
            Block.box(2, 4, 2, 15, 8, 14),
            Block.box(0, 8, 2, 5, 10, 14),
            Block.box(0, 17, 2, 6, 19, 14),
            Block.box(0, 8, 2, 5, 17, 14)
    );



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

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (level.getEntitiesOfClass(ChairSeatEntity.class, new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1)).isEmpty()) {
            ChairSeatEntity seat = new ChairSeatEntity(level, pos);
            level.addFreshEntity(seat);
            player.startRiding(seat);
        }

        return InteractionResult.CONSUME;
    }
}
