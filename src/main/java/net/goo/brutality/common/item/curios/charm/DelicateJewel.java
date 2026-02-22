package net.goo.brutality.common.item.curios.charm;

import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.List;

public class DelicateJewel extends BrutalityCurioItem {

    public DelicateJewel(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    public static void damageItemOnSpellCast(Player player, ICuriosItemHandler handler) {
        handler.findFirstCurio(BrutalityItems.DELICATE_JEWEL.get()).ifPresent(slotResult -> {
            slotResult.stack().hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        });
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 100;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.MENDING || enchantment == Enchantments.UNBREAKING;
    }
}
