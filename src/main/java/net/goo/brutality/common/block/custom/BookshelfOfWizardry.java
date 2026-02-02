package net.goo.brutality.common.block.custom;

import net.goo.brutality.common.block.IBrutalityMagicBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BookshelfOfWizardry extends Block implements IBrutalityMagicBlock {
    public BookshelfOfWizardry(Properties pProperties) {
        super(pProperties);
    }

    // Enchant power is handled in super which uses tags


    @Override
    public float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        return 2;
    }

    @Override
    public int getMagicPower(BlockState state) {
        return 5;
    }

    @Override
    public int getMaxCount() {
        return 15;
    }
}
