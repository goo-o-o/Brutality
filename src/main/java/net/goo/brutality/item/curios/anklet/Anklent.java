package net.goo.brutality.item.curios.anklet;

import net.goo.brutality.item.base.BrutalityAnkletItem;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public class Anklent extends BrutalityAnkletItem {


    public Anklent(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        String uuidSource = slotContext.identifier() + ":" + slotContext.index();
        UUID modifierUUID = UUID.nameUUIDFromBytes(uuidSource.getBytes(StandardCharsets.UTF_8));
        CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent(handler ->
                handler.addTransientSlotModifier(
                        "ring",
                        modifierUUID,
                        "CurioSlotGranting",
                        1,
                        AttributeModifier.Operation.ADDITION
                )
        );
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        String uuidSource = slotContext.identifier() + ":" + slotContext.index();
        UUID modifierUUID = UUID.nameUUIDFromBytes(uuidSource.getBytes(StandardCharsets.UTF_8));
        CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent(handler ->
                handler.removeSlotModifier("ring", modifierUUID)
        );
    }

}
