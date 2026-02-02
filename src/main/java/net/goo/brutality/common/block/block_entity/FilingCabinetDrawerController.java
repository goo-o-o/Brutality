package net.goo.brutality.common.block.block_entity;

import net.minecraft.world.level.block.entity.ChestLidController;

public class FilingCabinetDrawerController extends ChestLidController {
    private boolean isUpper;

    public FilingCabinetDrawerController(boolean isUpper) {
        this.isUpper = isUpper;
    }

}
