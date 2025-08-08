package net.goo.brutality.item.armor;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.base.BrutalityArmorItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class NoirArmorItem extends BrutalityArmorItem {


    public NoirArmorItem(ArmorMaterial pMaterial, Type pType, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pMaterial, pType, rarity, descriptionComponents);
    }
}
