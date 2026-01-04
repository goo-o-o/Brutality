package net.goo.brutality.item.curios.hands;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.curios.base.BaseHandsCurio;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeMod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class PhantomFinger extends BaseHandsCurio {


    public PhantomFinger(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID PHANTOM_FINGER_ENTITY_REACH = UUID.fromString("03337d07-7d18-44a6-9cb3-9ac9e9c35795");
    UUID PHANTOM_FINGER_BLOCK_REACH = UUID.fromString("db83c9de-a9f2-40a2-9f9a-f21f7699aebf");
    UUID PHANTOM_FINGER_RING_SLOT_UUID = UUID.fromString("bfaf8847-fff9-47ce-a4ba-60f1f4ec90de");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(PHANTOM_FINGER_ENTITY_REACH, "Reach Buff", 2, AttributeModifier.Operation.ADDITION));
        builder.put(ForgeMod.BLOCK_REACH.get(), new AttributeModifier(PHANTOM_FINGER_BLOCK_REACH, "Reach Buff", 2, AttributeModifier.Operation.ADDITION));
        return builder.build();

    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent(handler ->
                handler.addTransientSlotModifier("ring", PHANTOM_FINGER_RING_SLOT_UUID, "Ring Slot", 2, AttributeModifier.Operation.ADDITION));
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent(handler -> handler.removeSlotModifier("ring", PHANTOM_FINGER_RING_SLOT_UUID));
    }
}
