package net.goo.brutality.common.block;

import net.goo.brutality.common.block.custom.ManaCandle;
import net.goo.brutality.common.registry.BrutalityBlocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;


public interface IBrutalityMagicBlock {
    int getMagicPower(BlockState state);

    // Default to NONE if not part of a specific group
    default MagicBlockGroup getGroup() {
        return MagicBlockGroup.MISC;
    }

    enum MagicBlockGroup {
        CANDLE(5, () -> new BlockState[]{
                BrutalityBlocks.MANA_CANDLE.get().defaultBlockState().setValue(ManaCandle.CANDLES, 1),
                BrutalityBlocks.MANA_CANDLE.get().defaultBlockState().setValue(ManaCandle.CANDLES, 2),
                BrutalityBlocks.MANA_CANDLE.get().defaultBlockState().setValue(ManaCandle.CANDLES, 3),
                BrutalityBlocks.MANA_CANDLE.get().defaultBlockState().setValue(ManaCandle.CANDLES, 4)
        }),
        BOOKSHELF(15, () -> new BlockState[]{
                BrutalityBlocks.BOOKSHELF_OF_WIZARDRY.get().defaultBlockState()
        }),
        CRYSTAL(10, () -> new BlockState[]{
                BrutalityBlocks.SMALL_MANA_CRYSTAL_BUD.get().defaultBlockState(),
                BrutalityBlocks.MEDIUM_MANA_CRYSTAL_BUD.get().defaultBlockState(),
                BrutalityBlocks.LARGE_MANA_CRYSTAL_BUD.get().defaultBlockState(),
                BrutalityBlocks.MANA_CRYSTAL_CLUSTER.get().defaultBlockState(),
                BrutalityBlocks.MANA_CRYSTAL_BLOCK.get().defaultBlockState()
        }),
        MISC(25, () -> new BlockState[0]);

        public final int maximumBlockCount;
        private final Supplier<BlockState[]> blockStatesSupplier;

        MagicBlockGroup(int maximumBlockCount, Supplier<BlockState[]> blockStatesSupplier) {
            this.maximumBlockCount = maximumBlockCount;
            this.blockStatesSupplier = blockStatesSupplier;
        }

        public BlockState[] getBlockStates() {
            return blockStatesSupplier.get();
        }
    }
}


