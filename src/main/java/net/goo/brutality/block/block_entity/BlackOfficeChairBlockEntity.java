package net.goo.brutality.block.block_entity;

import net.goo.brutality.registry.BrutalityModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlackOfficeChairBlockEntity extends WhiteOfficeChairBlockEntity{
    public BlackOfficeChairBlockEntity(BlockEntityType<? extends BlackOfficeChairBlockEntity> entityType, BlockPos pos, BlockState state) {
        super(entityType, pos, state);
    }
    public static BlackOfficeChairBlockEntity create(BlockPos pos, BlockState state) {
        return new BlackOfficeChairBlockEntity(BrutalityModBlockEntities.BLACK_OFFICE_CHAIR_BLOCK_ENTITY.get(), pos, state);
    }

}
