package net.goo.brutality.item.curios.charm;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class EssentialOils extends BrutalityCurioItem {


    public EssentialOils(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }


    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null && slotContext.entity().tickCount % 10 == 0) {
            LivingEntity livingEntity = slotContext.entity();
            livingEntity.addEffect(new MobEffectInstance(BrutalityModMobEffects.OILED.get(), 11, 1, false, false));
        }
    }
}
