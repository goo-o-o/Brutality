package net.goo.brutality.common.block;

import net.minecraft.world.level.block.state.BlockState;

public interface IBrutalityMagicBlock {
    // How much power each block gives
    int getMagicPower(BlockState state);

    // How many of these types of blocks are allowed
    int getMaxCount();
}
