package net.goo.brutality.common.item.curios.feet;

import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class FlameWalker extends BrutalityCurioItem {
    public FlameWalker(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity livingEntity = slotContext.entity();
        if (!livingEntity.onGround()) return;
        Level level = livingEntity.level();
        if (livingEntity.getFeetBlockState().isAir() && FireBlock.canBePlacedAt(level, livingEntity.blockPosition(), Direction.NORTH)) {
            livingEntity.level().setBlock(livingEntity.blockPosition(), Blocks.FIRE.defaultBlockState(), 3);
        }

        livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 10, 0));
    }
}
