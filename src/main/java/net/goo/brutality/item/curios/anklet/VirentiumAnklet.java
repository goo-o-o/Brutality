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
import net.minecraftforge.common.ForgeMod;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class VirentiumAnklet extends BrutalityAnkletItem {


    public VirentiumAnklet(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.ANKLET;
    }

    UUID VIRENTIUM_ANKLET_DODGE_UUID = UUID.fromString("2f667ea3-692b-4759-95ee-c640d4b2f080");
    UUID VIRENTIUM_ANKLET_SWIM_SPEED_UUID = UUID.fromString("9600ba56-d097-4635-872a-0e9ad8630b62");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ModAttributes.DODGE_CHANCE.get(), new AttributeModifier(VIRENTIUM_ANKLET_DODGE_UUID, "Dodge Buff", 0.15, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(ForgeMod.SWIM_SPEED.get(), new AttributeModifier(VIRENTIUM_ANKLET_SWIM_SPEED_UUID, "Swim Speed Buff", 1, AttributeModifier.Operation.MULTIPLY_TOTAL));
            return builder.build();

        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }


}
