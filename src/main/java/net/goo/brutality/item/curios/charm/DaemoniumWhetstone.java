package net.goo.brutality.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.curios.base.BaseCharmCurio;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class DaemoniumWhetstone extends BaseCharmCurio {
    public DaemoniumWhetstone(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID DAEMONIUM_WHETSTONE_ARMOR_PEN_UUID = UUID.fromString("1574cbcf-e1ef-4d47-ac7c-726da719955c");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ModAttributes.ARMOR_PENETRATION.get(), new AttributeModifier(DAEMONIUM_WHETSTONE_ARMOR_PEN_UUID, "Armor Pen Buff", 0.13, AttributeModifier.Operation.MULTIPLY_BASE));
        return builder.build();
    }
}
