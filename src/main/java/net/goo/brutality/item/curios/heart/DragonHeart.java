package net.goo.brutality.item.curios.heart;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class DragonHeart extends BrutalityCurioItem {

    public DragonHeart(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.HEART;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);

        slotContext.entity().addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 3, 2));
        slotContext.entity().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3, 0));

    }
}
