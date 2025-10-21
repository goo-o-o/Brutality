package net.goo.brutality.block;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;

public interface PreciseRotatableBlock {
    int MAX = RotationSegment.getMaxSegmentIndex();
    int ROTATIONS = MAX + 1;
    IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;


    interface RotatableBlockEntity {
        String ROTATION_KEY = "Rotation";

        int getRotation();

    }
}

