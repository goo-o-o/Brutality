package net.goo.brutality.common.item.curios.hands;

import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.Set;

public class PerfectCell extends BrutalityCurioItem {


    public PerfectCell(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    private static final Set<MobEffect> DENIED_EFFECTS = Set.of(BrutalityEffects.MANA_FATIGUE.get(),
            BrutalityEffects.SIPHONED.get(), BrutalityEffects.RADIATION.get(), TerramityModMobEffects.ELECTRIC_SHOCK_EFFECT.get(),
            BrutalityEffects.PULVERIZED.get(), BrutalityEffects.MAGIC_SICKNESS.get());

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null && slotContext.entity().tickCount % 10 == 0) {
            slotContext.entity().addEffect(new MobEffectInstance(TerramityModMobEffects.IMMUNITY.get(), 11, 0));
        }
    }

    public static void denyEffect(MobEffectEvent.Applicable event) {
        CuriosApi.getCuriosInventory(event.getEntity()).ifPresent(handler -> {
            if (handler.isEquipped(BrutalityItems.PERFECT_CELL.get())) {
                if (DENIED_EFFECTS.contains(event.getEffectInstance().getEffect())) {
                    event.setCanceled(true);
                }
            }
        });
    }
}
