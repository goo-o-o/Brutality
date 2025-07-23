package net.goo.brutality.item.block;

import net.goo.brutality.item.base.BrutalityBlockItem;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class WaterCoolerBlockItem extends BrutalityBlockItem {

    public WaterCoolerBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }
}
