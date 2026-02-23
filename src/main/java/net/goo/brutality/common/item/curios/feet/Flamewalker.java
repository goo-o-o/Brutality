package net.goo.brutality.common.item.curios.feet;

import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Blocks;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class Flamewalker extends BrutalityCurioItem {
    public Flamewalker(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity livingEntity = slotContext.entity();
        if (livingEntity.getFeetBlockState().isAir()) {
            livingEntity.level().setBlock(livingEntity.getOnPos().above(), Blocks.FIRE.defaultBlockState(), 1);
        }
    }
}
