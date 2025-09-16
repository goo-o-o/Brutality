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

public class BigSteppa extends BrutalityAnkletItem {


    public BigSteppa(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.ANKLET;
    }

    UUID BIG_STEPPA_DODGE_UUID = UUID.fromString("ee0ef1d1-aaa1-4c67-8659-dadf877572b0");
    UUID BIG_STEPPA_STEP_HEIGHT_UUID = UUID.fromString("57882292-b2ea-45dc-a2f1-ba5adf6ca455");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ModAttributes.DODGE_CHANCE.get(), new AttributeModifier(BIG_STEPPA_DODGE_UUID, "Dodge Buff", 0.1, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(ForgeMod.STEP_HEIGHT_ADDITION.get(), new AttributeModifier(BIG_STEPPA_STEP_HEIGHT_UUID, "Step Height Buff", 1.5, AttributeModifier.Operation.ADDITION));
            return builder.build();

        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }

}
