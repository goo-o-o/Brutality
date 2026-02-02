package net.goo.brutality.common.item.base;

import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class BrutalityAnkletItem extends BrutalityCurioItem implements ICurioItem {

    public BrutalityAnkletItem(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }
    public BrutalityAnkletItem(Rarity rarity) {
        super(rarity);
    }

    public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
    }
    public void onDodgeClient(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
    }
}
