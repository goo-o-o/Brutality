package net.goo.brutality.item.curios.charm;

import net.goo.brutality.item.curios.BrutalityCurioItem;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class CartonOfPrismSolutionMilk extends BrutalityCurioItem {


    public CartonOfPrismSolutionMilk(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);

        if (slotContext.entity().tickCount % 40 == 0) {
            LivingEntity wearer = slotContext.entity();
            wearer.addEffect(new MobEffectInstance(TerramityModMobEffects.IMMUNITY.get(), 41, 0, false, true));
            wearer.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 41, 1, false, true));
        }
    }
}
