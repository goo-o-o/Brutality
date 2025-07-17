package net.goo.brutality.item.curios;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class AdditionCharm extends BrutalityCurioItem {

    public AdditionCharm(String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(identifier, rarity, descriptionComponents);
    }

    UUID ADDITION_CHARM_AD_UUID = UUID.fromString("f1c08e32-8553-4509-a864-00c92f5a7631");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ADDITION_CHARM_AD_UUID, "AD Buff", 3, AttributeModifier.Operation.ADDITION));
            return builder.build();

        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }
}
