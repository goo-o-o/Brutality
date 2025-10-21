package net.goo.brutality.block.custom;

import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.goo.brutality.registry.BrutalityModItems;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class DustbinBlock extends Block implements WorldlyContainerHolder {
    public static final int MAX_LEVEL = 15;
    public static final IntegerProperty PAPERS = IntegerProperty.create("papers", 0, MAX_LEVEL + 1);
    public static final Object2FloatMap<ItemLike> DISCARDABLES = new Object2FloatOpenHashMap<>();
    private static final VoxelShape OUTER_SHAPE = Block.box(2, 0, 2, 14, 16, 14);
    private static final VoxelShape[] SHAPES = Util.make(new VoxelShape[MAX_LEVEL + 2], (voxelShapes) -> {
        for (int i = 0; i < MAX_LEVEL + 1; ++i) {
            voxelShapes[i] = Shapes.join(OUTER_SHAPE, Block.box(2.5D, Math.max(0.1, i), 2.5D, 13.5D, 16.0D, 13.5D), BooleanOp.ONLY_FIRST);
        }
        voxelShapes[MAX_LEVEL + 1] = voxelShapes[MAX_LEVEL];
    });

    public static void bootStrap() {
        DISCARDABLES.defaultReturnValue(-1.0F);
        add(0.3F, Items.PAPER);
        add(0.75F, Items.ENCHANTED_BOOK);
        add(0.7F, BrutalityModItems.IMPORTANT_DOCUMENTS.get());
        add(0.5F, Items.BOOK);
        add(0.5F, Items.WRITABLE_BOOK);
        add(0.5F, Items.WRITTEN_BOOK);
    }

    private static void add(float pChance, ItemLike pItem) {
        DISCARDABLES.put(pItem.asItem(), pChance);
    }


    public DustbinBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(PAPERS, 0));
    }

    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPES[pState.getValue(PAPERS)];
    }

    public VoxelShape getInteractionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return OUTER_SHAPE;
    }

    public @NotNull VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPES[0];
    }


    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (pState.getValue(PAPERS) == MAX_LEVEL) {
            pLevel.scheduleTick(pPos, pState.getBlock(), 20);
        }
    }


    @Override
    public @NotNull InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        int i = pState.getValue(PAPERS);
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (i < MAX_LEVEL + 1 && DISCARDABLES.containsKey(itemstack.getItem())) {
            if (i < MAX_LEVEL && !pLevel.isClientSide) {
                BlockState blockstate = addItem(pPlayer, pState, pLevel, pPos, itemstack);
                pLevel.playLocalSound(pPos, pState != blockstate ? SoundEvents.COMPOSTER_FILL_SUCCESS : SoundEvents.COMPOSTER_FILL, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                pPlayer.awardStat(Stats.ITEM_USED.get(itemstack.getItem()));
                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
            }

            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        } else if (i == MAX_LEVEL + 1) {
            extractOutput(pPlayer, pState, pLevel, pPos);
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }



    public static void extractOutput(Entity pEntity, BlockState pState, Level pLevel, BlockPos pPos) {
        if (!pLevel.isClientSide) {
            Vec3 vec3 = Vec3.atLowerCornerWithOffset(pPos, 0.5D, 1.01D, 0.5D).offsetRandom(pLevel.random, 0.7F);
            ItemEntity itementity = new ItemEntity(pLevel, vec3.x(), vec3.y(), vec3.z(), new ItemStack(Items.BONE_MEAL)); // TODO: Make a Crumpled Paper Throwable
            itementity.setDefaultPickUpDelay();
            pLevel.addFreshEntity(itementity);
        }

        empty(pEntity, pState, pLevel, pPos);
        pLevel.playSound(null, pPos, SoundEvents.COMPOSTER_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    static BlockState empty(@Nullable Entity pEntity, BlockState pState, LevelAccessor pLevel, BlockPos pPos) {
        BlockState blockstate = pState.setValue(PAPERS, 0);
        pLevel.setBlock(pPos, blockstate, 3);
        pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pEntity, blockstate));
        return blockstate;
    }

    static BlockState addItem(@Nullable Entity pEntity, BlockState pState, LevelAccessor pLevel, BlockPos pPos, ItemStack pStack) {
        int i = pState.getValue(PAPERS);
        float f = DISCARDABLES.getFloat(pStack.getItem());
        if ((i != 0 || !(f > 0.0F)) && !(pLevel.getRandom().nextDouble() < (double) f)) {
            return pState;
        } else {
            int j = i + 1;
            BlockState blockstate = pState.setValue(PAPERS, j);
            pLevel.setBlock(pPos, blockstate, 3);
            pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pEntity, blockstate));
            if (j == MAX_LEVEL) {
                pLevel.scheduleTick(pPos, pState.getBlock(), 20);
            }

            return blockstate;
        }
    }

    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(PAPERS) == MAX_LEVEL) {
            pLevel.setBlock(pPos, pState.cycle(PAPERS), 3);
            pLevel.playSound(null, pPos, SoundEvents.COMPOSTER_READY, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }



    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState pBlockState, Level pLevel, BlockPos pPos) {
        return pBlockState.getValue(PAPERS);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(PAPERS);
    }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return false;
    }

    @Override
    public @NotNull WorldlyContainer getContainer(BlockState pState, LevelAccessor pLevel, BlockPos pPos) {
        int i = pState.getValue(PAPERS);
        if (i == MAX_LEVEL + 1) {
            return new DustbinBlock.OutputContainer(pState, pLevel, pPos, new ItemStack(Items.BONE_MEAL)); // TODO: Make Crumbled Paper Throwable
        } else {
            return i < MAX_LEVEL ? new DustbinBlock.InputContainer(pState, pLevel, pPos) : new DustbinBlock.EmptyContainer();
        }
    }

    static class EmptyContainer extends SimpleContainer implements WorldlyContainer {
        public EmptyContainer() {
            super(0);
        }

        public int @NotNull [] getSlotsForFace(Direction pSide) {
            return new int[0];
        }

        public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @Nullable Direction pDirection) {
            return false;
        }

        public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
            return false;
        }
    }

    static class InputContainer extends SimpleContainer implements WorldlyContainer {
        private final BlockState state;
        private final LevelAccessor level;
        private final BlockPos pos;
        private boolean changed;

        public InputContainer(BlockState pState, LevelAccessor pLevel, BlockPos pPos) {
            super(1);
            this.state = pState;
            this.level = pLevel;
            this.pos = pPos;
        }

        public int getMaxStackSize() {
            return 1;
        }

        public int @NotNull [] getSlotsForFace(Direction pSide) {
            return pSide == Direction.UP ? new int[]{0} : new int[0];
        }

        public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @Nullable Direction pDirection) {
            return !this.changed && pDirection == Direction.UP && ComposterBlock.COMPOSTABLES.containsKey(pItemStack.getItem());
        }

        public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
            return false;
        }

        public void setChanged() {
            ItemStack itemstack = this.getItem(0);
            if (!itemstack.isEmpty()) {
                this.changed = true;
                BlockState blockstate = DustbinBlock.addItem(null, this.state, this.level, this.pos, itemstack);
                this.level.levelEvent(1500, this.pos, blockstate != this.state ? 1 : 0);
                this.removeItemNoUpdate(0);
            }

        }
    }

    static class OutputContainer extends SimpleContainer implements WorldlyContainer {
        private final BlockState state;
        private final LevelAccessor level;
        private final BlockPos pos;
        private boolean changed;

        public OutputContainer(BlockState pState, LevelAccessor pLevel, BlockPos pPos, ItemStack pStack) {
            super(pStack);
            this.state = pState;
            this.level = pLevel;
            this.pos = pPos;
        }
        public int getMaxStackSize() {
            return 1;
        }

        public int @NotNull [] getSlotsForFace(Direction pSide) {
            return pSide == Direction.DOWN ? new int[]{0} : new int[0];
        }
        public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @Nullable Direction pDirection) {
            return false;
        }

        public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
            return !this.changed && pDirection == Direction.DOWN && pStack.is(Items.BONE_MEAL); // TODO: Make a Crumpled Paper Throwable
        }

        public void setChanged() {
            DustbinBlock.empty(null, this.state, this.level, this.pos);
            this.changed = true;
        }
    }

    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return true;
    }


}
