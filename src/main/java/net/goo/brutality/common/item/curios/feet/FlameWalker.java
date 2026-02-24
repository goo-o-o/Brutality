package net.goo.brutality.common.item.curios.feet;

import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Blocks;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class FlameWalker extends BrutalityCurioItem {
    public FlameWalker(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity livingEntity = slotContext.entity();
        if (livingEntity.tickCount % 2 != 0 || !livingEntity.onGround()) return;
        BlockPos current = livingEntity.getOnPos().above();
        BlockPos previous = new BlockPos((int) livingEntity.xo, (int) livingEntity.yo, (int) livingEntity.zo);
        if (!current.equals(previous)) {
            // entity has moved to a new block
            // we can now safely set it on fire
            if (livingEntity.level().getBlockState(previous).isAir()) {
                livingEntity.level().setBlock(previous, Blocks.FIRE.defaultBlockState(), 1);
            }
        }
    }
}
