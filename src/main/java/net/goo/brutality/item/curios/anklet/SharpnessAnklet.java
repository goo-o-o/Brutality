package net.goo.brutality.item.curios.anklet;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.base.BrutalityAnkletItem;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class SharpnessAnklet extends BrutalityAnkletItem {


    public SharpnessAnklet(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID SHARPNESS_ANKLET_DODGE_UUID = UUID.fromString("f22b0f7c-2446-4f4b-9c00-1d8e1d384542");
    UUID SHARPNESS_ANKLET_AD_UUID = UUID.fromString("d0da48d2-c239-4a59-ba43-bd4f8a424886");
    UUID SHARPNESS_ANKLET_PIERCING_UUID = UUID.fromString("d0da48d2-c239-4a59-ba43-bd4f8a424886");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ModAttributes.DODGE_CHANCE.get(),
                new AttributeModifier(SHARPNESS_ANKLET_DODGE_UUID, "Dodge Buff", -0.05, AttributeModifier.Operation.MULTIPLY_BASE));
        builder.put(Attributes.ATTACK_DAMAGE,
                new AttributeModifier(SHARPNESS_ANKLET_AD_UUID, "AD Buff", 3, AttributeModifier.Operation.ADDITION));
        builder.put(ModAttributes.PIERCING_DAMAGE.get(),
                new AttributeModifier(SHARPNESS_ANKLET_PIERCING_UUID, "Piercing Damage Buff", 0.2, AttributeModifier.Operation.MULTIPLY_TOTAL));

        return builder.build();

    }

}
