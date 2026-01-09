package net.goo.brutality.item.curios.hands;

import net.goo.brutality.item.curios.BrutalityCurioItem;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.Set;

public class PerfectCell extends BrutalityCurioItem {


    public PerfectCell(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

   public static Set<MobEffect> DENIED_EFFECTS = Set.of(BrutalityModMobEffects.MANA_FATIGUE.get(),
            BrutalityModMobEffects.SIPHONED.get(), BrutalityModMobEffects.RADIATION.get(), TerramityModMobEffects.ELECTRIC_SHOCK_EFFECT.get(),
            BrutalityModMobEffects.PULVERIZED.get(), BrutalityModMobEffects.MAGIC_SICKNESS.get());

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null && slotContext.entity().tickCount % 10 == 0) {
            slotContext.entity().addEffect(new MobEffectInstance(TerramityModMobEffects.IMMUNITY.get(), 11, 0));
        }
    }
}
