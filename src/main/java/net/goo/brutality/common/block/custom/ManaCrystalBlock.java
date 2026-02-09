package net.goo.brutality.common.block.custom;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.block.IBrutalityMagicBlock;
import net.goo.brutality.common.registry.BrutalityBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ManaCrystalBlock extends BuddingAmethystBlock implements IBrutalityMagicBlock {
    private static final Direction[] DIRECTIONS = Direction.values();
    public static final BooleanProperty WAXED = BooleanProperty.create("waxed");

    public ManaCrystalBlock() {
        super(Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.COLOR_BLUE).sound(SoundType.AMETHYST).strength(10.0F, 500.0F).lightLevel((s) -> 12).requiresCorrectToolForDrops().hasPostProcess((bs, br, bp) -> true).emissiveRendering((bs, br, bp) -> true).randomTicks());

        this.registerDefaultState(this.stateDefinition.any().setValue(WAXED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(WAXED);
    }

    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pState.getValue(WAXED)) {
            if (pRandom.nextInt(5) == 0) {
                Direction direction = DIRECTIONS[pRandom.nextInt(DIRECTIONS.length)];
                BlockPos blockpos = pPos.relative(direction);
                BlockState blockstate = pLevel.getBlockState(blockpos);
                Block block = null;
                if (canClusterGrowAtState(blockstate)) {
                    block = BrutalityBlocks.SMALL_MANA_CRYSTAL_BUD.get();
                } else if (blockstate.is(BrutalityBlocks.SMALL_MANA_CRYSTAL_BUD.get()) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
                    block = BrutalityBlocks.MEDIUM_MANA_CRYSTAL_BUD.get();
                } else if (blockstate.is(BrutalityBlocks.MEDIUM_MANA_CRYSTAL_BUD.get()) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
                    block = BrutalityBlocks.LARGE_MANA_CRYSTAL_BUD.get();
                } else if (blockstate.is(BrutalityBlocks.LARGE_MANA_CRYSTAL_BUD.get()) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
                    block = BrutalityBlocks.MANA_CRYSTAL_CLUSTER.get();
                }

                if (block != null) {
                    BlockState blockState = block.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction).setValue(AmethystClusterBlock.WATERLOGGED, Boolean.valueOf(blockstate.getFluidState().getType() == Fluids.WATER));
                    pLevel.setBlockAndUpdate(blockpos, blockState);
                }

            }
        }
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        pTooltip.add(Component.translatable("block." + Brutality.MOD_ID + ".mana_crystal_block.description.1"));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (itemstack.is(Items.HONEYCOMB) && !state.getValue(WAXED)) {
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }

            level.setBlock(pos, state.setValue(WAXED, true), 11);
            level.levelEvent(player, 3003, pos, 0); // Plays the waxing sound/particles

            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    public int getMagicPower(BlockState state) {
        return 10;
    }

    @Override
    public int getMaxCount() {
        return 10;
    }
}
