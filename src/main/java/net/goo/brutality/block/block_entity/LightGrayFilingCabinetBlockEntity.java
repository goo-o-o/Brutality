package net.goo.brutality.block.block_entity;

import net.goo.brutality.registry.BrutalityModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LightGrayFilingCabinetBlockEntity extends WhiteFilingCabinetBlockEntity{
    public LightGrayFilingCabinetBlockEntity(BlockEntityType<? extends WhiteFilingCabinetBlockEntity> entityType, BlockPos pPos, BlockState pBlockState) {
        super(entityType, pPos, pBlockState);
    }

    public static LightGrayFilingCabinetBlockEntity create(BlockPos pos, BlockState state) {
        return new LightGrayFilingCabinetBlockEntity(BrutalityModBlockEntities.LIGHT_GRAY_FILING_CABINET_BLOCK_ENTITY.get(), pos, state);
    }

    public static <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? createTickerHelper(blockEntityType, BrutalityModBlockEntities.LIGHT_GRAY_FILING_CABINET_BLOCK_ENTITY.get(), LightGrayFilingCabinetBlockEntity::tickDrawer) : null;
    }
}
