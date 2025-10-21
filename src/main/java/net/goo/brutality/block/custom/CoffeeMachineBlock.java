package net.goo.brutality.block.custom;

import net.goo.brutality.block.block_entity.CoffeeMachineBlockEntity;
import net.goo.brutality.registry.BrutalityModBlockEntities;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.util.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

public class CoffeeMachineBlock extends BaseEntityBlock {
    public CoffeeMachineBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));

    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
    }

    public static final IntegerProperty ANIMATION = IntegerProperty.create("animation", 0, 13);
    public static DirectionProperty FACING;

    private static final VoxelShape SHAPE = box(1, 0, 1, 15, 16, 15);

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
        return new CoffeeMachineBlockEntity(BrutalityModBlockEntities.COFFEE_MACHINE_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }


    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        if (pStack.hasCustomHoverName()) {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof CoffeeMachineBlockEntity blockEntity) {
                blockEntity.setCustomName(pStack.getHoverName());
            }
        }

    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(BrutalityModItems.MUG.get())) {
            if (!level.isClientSide() && level.getBlockEntity(pos) instanceof CoffeeMachineBlockEntity be) {
                if (be.hasCustomName()) {
                    Component customName = be.getCustomName();
                    if (customName != null && customName.contains(Component.translatable("block.brutality.eeffoc_machine"))) {
                        if (ModUtils.getTextureIdx(stack) == 0) {
                            ModUtils.setTextureIdx(stack, 2);
                            be.triggerAnim("controller", "right_click");
                            level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS);
                            return InteractionResult.sidedSuccess(true);
                        }
                    }
                }
                if (ModUtils.getTextureIdx(stack) == 0) {
                    ModUtils.setTextureIdx(stack, 1);
                    be.triggerAnim("controller", "right_click");
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS);
                    return InteractionResult.sidedSuccess(level.isClientSide());
                }
            }
        }
        return InteractionResult.FAIL;
    }
}
