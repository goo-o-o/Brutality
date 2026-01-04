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

public class DeadshotBrooch extends BaseCharmCurio {


    public DeadshotBrooch(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID DEADSHOT_BROOCH_CRITICAL_CHANCE_UUID = UUID.fromString("b84f46c5-9ac2-4bab-bbd2-ada485d732bc");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ModAttributes.CRITICAL_STRIKE_CHANCE.get(), new AttributeModifier(DEADSHOT_BROOCH_CRITICAL_CHANCE_UUID,
                "Crit Chance Boost", 0.15, AttributeModifier.Operation.MULTIPLY_BASE));
        return builder.build();
    }

}
