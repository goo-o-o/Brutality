package net.goo.brutality.common.item.curios.necklace;

import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;

public class FuzzyDice extends BrutalityCurioItem {
    public FuzzyDice(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    /**
     * Handles logic for "Double Criticals" triggered by specific gear.
     * <p>
     * <b>Note:</b> This should only be invoked if a Brutality Critical Strike has already
     * been confirmed (isBrutalityCrit == true). It provides a second roll to double the
     * existing critical damage modifier.
     * </p>
     *
     * @param player       The attacking player.
     * @param event        The crit event to modify.
     * @param actualChance The calculated crit chance (Attribute - 1.0) used for the second roll.
     */
    public static void handleDoubleCrit(Player player, CriticalHitEvent event, float actualChance) {
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            // Fuzzy Dice: Roll again. On success, double the current damage modifier.
            if (handler.isEquipped(BrutalityItems.FUZZY_DICE.get())) {
                if (player.getRandom().nextFloat() < actualChance) {
                    event.setDamageModifier(event.getDamageModifier() * 2.0F);

                }
            }
        });
    }

}
