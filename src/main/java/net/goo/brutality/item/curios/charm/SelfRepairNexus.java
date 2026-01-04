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

public class SelfRepairNexus extends BaseCharmCurio {


    public SelfRepairNexus(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID SELF_REPAIR_NEXUS_LIFESTEAL_UUID = UUID.fromString("e9b30c40-eaf9-475c-ac92-905f76983256");
    UUID SELF_REPAIR_NEXUS_HEALTH_UUID = UUID.fromString("a130ccf5-aac9-447a-b519-fd38057772e7");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ModAttributes.LIFESTEAL.get(), new AttributeModifier(SELF_REPAIR_NEXUS_LIFESTEAL_UUID,
                    "Lifesteal Boost", 0.05, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(Attributes.MAX_HEALTH, new AttributeModifier(SELF_REPAIR_NEXUS_HEALTH_UUID,
                    "Health Boost", 4, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }


}
