package net.goo.brutality.common.item.weapon.sword;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.base.BrutalitySwordItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TruthseekerSword extends BrutalitySwordItem {


    public TruthseekerSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }


}
