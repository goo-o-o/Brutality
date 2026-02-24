package net.goo.brutality.common.block.custom;

import net.goo.brutality.common.block.IBrutalityMagicBlock;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ManaCrystalCluster extends AmethystClusterBlock implements IBrutalityMagicBlock {
    public int magicPower;

    public ManaCrystalCluster(int pSize, int pOffset, int magicPower, Properties pProperties) {
        super(pSize, pOffset, pProperties);
        this.magicPower = magicPower;
    }

    @Override
    public int getMagicPower(BlockState state) {
        return this.magicPower;
    }

}
