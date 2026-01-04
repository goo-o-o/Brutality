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

public class WireCutters extends BaseCharmCurio {


    public WireCutters(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


    UUID WIRE_CUTTERS_ARMOR_PEN_UUID = UUID.fromString("f2235ea1-85b3-4d8b-a5c4-e5da054f4f47");
    UUID WIRE_CUTTERS_CRITICAL_DAMAGE_UUID = UUID.fromString("c736e8f3-e67a-41be-9c10-48ecb422ce0c");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ModAttributes.ARMOR_PENETRATION.get(), new AttributeModifier(WIRE_CUTTERS_ARMOR_PEN_UUID, "Armor Pen Buff", 0.05F, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(ModAttributes.CRITICAL_STRIKE_DAMAGE.get(), new AttributeModifier(WIRE_CUTTERS_CRITICAL_DAMAGE_UUID, "Crit Damage Debuff", 0.1F, AttributeModifier.Operation.MULTIPLY_BASE));
            return builder.build();
    }

}
