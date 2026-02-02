package net.goo.brutality.common.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

public class OfficeCarpetBlock extends WoolCarpetBlock {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    public OfficeCarpetBlock(DyeColor pColor, Properties pProperties) {
        super(pColor, pProperties);
        this.registerDefaultState(defaultBlockState().setValue(AXIS, Direction.Axis.X));
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
        pBuilder.add(AXIS);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction.Axis axis = pContext.getHorizontalDirection().getAxis();
        return defaultBlockState().setValue(AXIS, axis);
    }
}
