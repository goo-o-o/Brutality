package net.goo.brutality.item.curios.anklet;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityAnkletItem;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class WindsweptAnklet extends BrutalityAnkletItem {


    public WindsweptAnklet(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.ANKLET;
    }

    UUID WINDSWEPT_ANKLET_DODGE_UUID = UUID.fromString("98d529f9-7907-4910-9743-b10fcb4a3dcd");
    UUID WINDSWEPT_ANKLET_JUMP_UUID = UUID.fromString("381dc366-f824-44bf-a988-c314f22b3dde");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ModAttributes.DODGE_CHANCE.get(),
                    new AttributeModifier(WINDSWEPT_ANKLET_DODGE_UUID, "Dodge Buff", 0.15, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(ModAttributes.JUMP_HEIGHT.get(),
                    new AttributeModifier(WINDSWEPT_ANKLET_JUMP_UUID, "Jump Buff", 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL));
            return builder.build();

        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }

}
