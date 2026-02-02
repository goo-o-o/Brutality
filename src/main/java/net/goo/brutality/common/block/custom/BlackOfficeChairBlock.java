package net.goo.brutality.common.block.custom;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.block.block_entity.BlackOfficeChairBlockEntity;
import net.goo.brutality.common.registry.BrutalityBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlackOfficeChairBlock extends WhiteOfficeChairBlock {

    public BlackOfficeChairBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlackOfficeChairBlockEntity(BrutalityBlockEntities.BLACK_OFFICE_CHAIR_BLOCK_ENTITY.get(), pPos, pState);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.translatable("block." + Brutality.MOD_ID + ".black_office_chair.description.1"));
    }
}