package net.goo.brutality.block.custom;

import net.goo.brutality.Brutality;
import net.goo.brutality.block.PreciseRotatableBlock;
import net.goo.brutality.block.block_entity.LCDMonitorBlockEntity;
import net.goo.brutality.registry.BrutalityModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LCDMonitorBlock extends BaseEntityBlock implements PreciseRotatableBlock, EntityBlock {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public LCDMonitorBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(ACTIVE, false).setValue(ROTATION, 0));

    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.translatable("block." + Brutality.MOD_ID + ".monitor.description.1"));
    }

    @Override
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 0.6F;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 10;
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(ROTATION,
                RotationSegment.convertToSegment(context.getRotation()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(ACTIVE, ROTATION);
    }

    //    public static final VoxelShape X_AABB = Block.box(0, 3.5, 7.5, 16, 13.5, 8.5);
//    public static final VoxelShape Z_AABB = Block.box(7.5, 3.5, 0, 8.5, 13.5, 16);
    public static final VoxelShape SHAPE = Block.box(7, 0, 7, 9, 13.5, 9);

    @Override
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
//        Direction direction = pState.getValue(FACING);
//        return (direction == Direction.SOUTH || direction == Direction.NORTH) ? X_AABB : Z_AABB;
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide()) return InteractionResult.SUCCESS;

        BlockState state = pState.cycle(ACTIVE);
        pLevel.setBlock(pPos, state, 3);
        float pitch = pState.getValue(ACTIVE) ? 0.5F : 0.7F;
        Vec3 loc = pHit.getLocation();
        pLevel.playSound(null, loc.x, loc.y, loc.z, BrutalityModSounds.LIGHT_SWITCH_MONO.get(), SoundSource.BLOCKS, 1F, pitch);

        return InteractionResult.CONSUME;
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(ROTATION, direction.rotate(state.getValue(ROTATION), ROTATIONS));
    }

    @Override
    public @NotNull BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.setValue(ROTATION, pMirror.mirror(pState.getValue(ROTATION), ROTATIONS));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new LCDMonitorBlockEntity(pPos, pState);
    }
}
