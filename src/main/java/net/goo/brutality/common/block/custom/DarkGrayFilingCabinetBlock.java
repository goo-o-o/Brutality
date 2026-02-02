package net.goo.brutality.common.block.custom;

import net.goo.brutality.common.block.block_entity.GrayFilingCabinetBlockEntity;
import net.goo.brutality.common.block.block_entity.LightGrayFilingCabinetBlockEntity;
import net.goo.brutality.common.registry.BrutalityBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DarkGrayFilingCabinetBlock extends WhiteFilingCabinetBlock{
    public DarkGrayFilingCabinetBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new GrayFilingCabinetBlockEntity(BrutalityBlockEntities.GRAY_FILING_CABINET_BLOCK_ENTITY.get(), pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return LightGrayFilingCabinetBlockEntity.getTicker(level, state, blockEntityType);
    }

}
