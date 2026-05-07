package net.goo.brutality.common.item.curios.ring;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class RingOfRings extends BrutalityCurioItem {
    public RingOfRings(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public double getDynamicAttributeBonus(SlotContext slotContext, ItemStack stack, AttributeInstance attributeInstance, double currentBonus) {
        if (attributeInstance.getAttribute() == Attributes.ARMOR || attributeInstance.getAttribute() == BrutalityAttributes.LETHALITY.get()) {
            return getRingCount(slotContext.entity());
        }
        return super.getDynamicAttributeBonus(slotContext, stack, attributeInstance, currentBonus);
    }

    private int getRingCount(LivingEntity livingEntity) {
        return CuriosApi.getCuriosInventory(livingEntity).map(handler -> handler.findCurios("ring").size()).orElse(0);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null && slotContext.entity().level().isClientSide()) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            LivingEntity player = slotContext.entity();

            double ringCount = getRingCount(player);

            builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor Buff", ringCount, AttributeModifier.Operation.ADDITION));
            builder.put(BrutalityAttributes.LETHALITY.get(), new AttributeModifier(uuid, "Lethality Buff", ringCount, AttributeModifier.Operation.ADDITION));


            return builder.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }


}
