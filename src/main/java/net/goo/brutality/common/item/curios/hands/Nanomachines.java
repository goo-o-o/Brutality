package net.goo.brutality.common.item.curios.hands;

import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.util.item.ItemCategoryUtils;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class Nanomachines extends BrutalityCurioItem {

    public Nanomachines(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public float onWearerHurt(LivingEntity wearer, ItemStack stack, DamageSource source, float amount) {
        if (source.is(DamageTypes.EXPLOSION)) return Math.min(4, amount);
        return amount;
    }

    public static float handleBluntDamage(ItemStack stack, float amount) {
        if (ItemCategoryUtils.isShovel(stack) || ItemCategoryUtils.isHammer(stack)) return Math.min(4, amount);
        return amount;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity().tickCount % 40 == 0) slotContext.entity().heal(1);
    }
}
