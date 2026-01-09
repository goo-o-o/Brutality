package net.goo.brutality.item.curios.belt;

import net.goo.brutality.item.curios.BrutalityCurioItem;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class MiniatureAnchor extends BrutalityCurioItem {


    public MiniatureAnchor(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null && slotContext.entity().tickCount % 10 == 0) {
            if (slotContext.entity().isUnderWater()) {
                slotContext.entity().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 11, 3));
            }
        }
    }
}
