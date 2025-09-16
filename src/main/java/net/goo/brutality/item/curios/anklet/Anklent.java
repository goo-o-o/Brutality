package net.goo.brutality.item.curios.anklet;

import net.goo.brutality.item.base.BrutalityAnkletItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class Anklent extends BrutalityAnkletItem {


    public Anklent(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID ANKLENT_RING_SLOT_UUID = UUID.fromString("fc7c6b48-90db-4b95-8c6d-631481fe406e");


    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent(handler ->
                handler.addTransientSlotModifier("ring", ANKLENT_RING_SLOT_UUID, "Ring Slot", 1, AttributeModifier.Operation.ADDITION));
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent(inventory -> inventory.removeSlotModifier("ring", ANKLENT_RING_SLOT_UUID));
    }
}
