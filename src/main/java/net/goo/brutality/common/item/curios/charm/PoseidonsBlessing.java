package net.goo.brutality.common.item.curios.charm;

import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Rarity;

import java.util.List;

public class PoseidonsBlessing extends BrutalityCurioItem {


    public PoseidonsBlessing(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    public static boolean shouldApply(LivingEntity entity) {
        return ModUtils.isWearingCurio(entity, BrutalityItems.POSEIDONS_BLESSING.get(), BrutalityItems.HYDROPHOBIC_NANOCOATING.get());
    }
}
