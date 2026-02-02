package net.goo.brutality.common.block.custom;

import net.goo.brutality.common.block.HorizontalDirectionalBlock;
import net.goo.brutality.common.block.block_entity.SuperSnifferFigureBlockEntity;
import net.goo.brutality.common.registry.BrutalityBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class SuperSnifferFigureBlock extends BaseEntityBlock {
    public SuperSnifferFigureBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));

    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
    }

    public static final IntegerProperty ANIMATION = IntegerProperty.create("animation", 0, 9);
    public static DirectionProperty FACING;

    private static final VoxelShape SHAPE = box(4, 0, 2, 13, 18, 14);

    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return true;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ANIMATION, FACING);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    public @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public @NotNull BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SuperSnifferFigureBlockEntity(BrutalityBlockEntities.SUPER_SNIFFER_FIGURE_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }


    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }


    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof SuperSnifferFigureBlockEntity be && hand == InteractionHand.MAIN_HAND) {
            if (player.isCrouching()) {
                be.setTexture((be.getTexture() + 1) % 4);
            } else {
                be.setPose((be.getPose() + 1) % 10);
            }
            level.playSound(null, pos, SoundEvents.WOOL_HIT, SoundSource.BLOCKS, 1, Mth.nextFloat(level.random, 0.8F, 1.2F));
            return InteractionResult.SUCCESS;


        }
        return InteractionResult.PASS;
    }


}
