package net.goo.brutality.common.item.curios.charm;

import net.goo.brutality.client.ClientProxy;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.util.EnvironmentColorManager;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class Error404 extends BrutalityCurioItem {


    public Error404(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        ClientProxy.safeReloadChunks(250);
        EnvironmentColorManager.setColor(EnvironmentColorManager.ColorType.SKY, FastColor.ARGB32.color(255, 248, 0, 248));
        EnvironmentColorManager.setColor(EnvironmentColorManager.ColorType.FOG, FastColor.ARGB32.color(255, 0, 0, 0));
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ClientProxy.safeReloadChunks(259);
        EnvironmentColorManager.resetAllColors();
    }
}
