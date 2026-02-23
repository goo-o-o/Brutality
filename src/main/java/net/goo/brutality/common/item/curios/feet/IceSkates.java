package net.goo.brutality.common.item.curios.feet;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.Tags;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class IceSkates extends BrutalityCurioItem {


    public IceSkates(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public double getDynamicAttributeBonus(LivingEntity owner, ItemStack stack, Attribute attribute, double currentBonus) {
        if (attribute == BrutalityAttributes.GROUND_FRICTION.get() && owner.getBlockStateOn().is(BlockTags.ICE)) {
            return -0.95F * owner.getAttributeBaseValue(BrutalityAttributes.GROUND_FRICTION.get());
        }
        return super.getDynamicAttributeBonus(owner, stack, attribute, currentBonus);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null && slotContext.entity().level().isClientSide()) {
            if (slotContext.entity().getBlockStateOn().is(BlockTags.ICE)) {
                ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
                builder.put(BrutalityAttributes.GROUND_FRICTION.get(), new AttributeModifier(uuid, "Friction Buff", -0.95F, AttributeModifier.Operation.MULTIPLY_BASE));

                return builder.build();
            }
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }
}
