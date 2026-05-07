package net.goo.brutality.common.item.weapon.sword;

import net.goo.brutality.common.item.base.BrutalitySwordItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;

import java.util.List;

public class WorldTreeSword extends BrutalitySwordItem {
    public WorldTreeSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    public static double getDamageBonus(Player player) {
        if (player.level().getBiome(player.blockPosition()).is(BiomeTags.IS_FOREST)) {
            return 10;
        }
        return 0;
    }
}
