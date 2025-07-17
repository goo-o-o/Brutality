package net.goo.brutality.item.curios;

import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class DragonHeartHeart extends BrutalityCurioItem {

    public DragonHeartHeart(Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);

        slotContext.entity().addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 3, 1));
        slotContext.entity().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3, 0));

    }
}
