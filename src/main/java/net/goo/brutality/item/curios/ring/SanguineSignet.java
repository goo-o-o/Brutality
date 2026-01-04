package net.goo.brutality.item.curios.ring;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.curios.base.BaseRingCurio;
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

public class SanguineSignet extends BaseRingCurio {


    public SanguineSignet(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID SANGUINE_SIGNET_MAX_HEALTH = UUID.fromString("f14cfed5-6d9e-49de-bdea-16f60d2161e2");
    UUID SANGUINE_SIGNET_LIFESTEAL = UUID.fromString("240a9aff-4e4e-4659-b10c-9d7c9afdbaa1");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(Attributes.MAX_HEALTH, new AttributeModifier(SANGUINE_SIGNET_MAX_HEALTH, "Max Health Buff", 2, AttributeModifier.Operation.ADDITION));
        builder.put(ModAttributes.LIFESTEAL.get(), new AttributeModifier(SANGUINE_SIGNET_LIFESTEAL, "Lifesteal Buff", 0.025F, AttributeModifier.Operation.MULTIPLY_BASE));
        return builder.build();

    }

}
