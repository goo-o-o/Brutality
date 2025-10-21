package net.goo.brutality.block.custom;

import net.goo.brutality.block.block_entity.GrayFilingCabinetBlockEntity;
import net.goo.brutality.registry.BrutalityModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GrayFilingCabinetBlock extends WhiteFilingCabinetBlock{
    public GrayFilingCabinetBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new GrayFilingCabinetBlockEntity(BrutalityModBlockEntities.GRAY_FILING_CABINET_BLOCK_ENTITY.get(), pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return GrayFilingCabinetBlockEntity.getTicker(level, state, blockEntityType);
    }

}
