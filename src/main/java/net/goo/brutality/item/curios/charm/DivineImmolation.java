package net.goo.brutality.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.curios.base.BaseCharmCurio;
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

public class DivineImmolation extends BaseCharmCurio {


    public DivineImmolation(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID DIVINE_IMMOLATION_CRIT_CHANCE_UUID = UUID.fromString("f04bdb78-32fa-451c-950e-334c3213fa13");
    UUID DIVINE_IMMOLATION_HEALTH_UUID = UUID.fromString("f04bdb78-32fa-451c-950e-334c3213fa13");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ModAttributes.CRITICAL_STRIKE_CHANCE.get(),
                new AttributeModifier(DIVINE_IMMOLATION_CRIT_CHANCE_UUID, "Crit Chance Buff", 1, AttributeModifier.Operation.MULTIPLY_BASE));
        builder.put(Attributes.MAX_HEALTH,
                new AttributeModifier(DIVINE_IMMOLATION_HEALTH_UUID, "Health Buff", -0.99, AttributeModifier.Operation.MULTIPLY_TOTAL));

        return builder.build();

    }
}
