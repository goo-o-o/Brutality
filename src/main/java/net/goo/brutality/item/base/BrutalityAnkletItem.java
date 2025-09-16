package net.goo.brutality.item.base;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class BrutalityAnkletItem extends BrutalityCurioItem implements ICurioItem {

    public BrutalityAnkletItem(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.ANKLET;
    }

    public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
    }
    public void onDodgeClient(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
    }
}
