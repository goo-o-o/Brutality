package net.goo.brutality.item.curios;

import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.registry.ModItems;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class PortableMiningRigCharm extends BrutalityCurioItem {
    public PortableMiningRigCharm(Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Entity entity = slotContext.entity();
        if (entity instanceof LivingEntity livingEntity && livingEntity.level().getRandom().nextIntBetweenInclusive(0, 100) < 2) {
            CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler ->
            handler.findFirstCurio(ModItems.)
            );
        }



        super.curioTick(slotContext, stack);
    }
}

