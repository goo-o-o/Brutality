package net.goo.brutality.common.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class EmergencyFlask extends BrutalityCurioItem {


    public EmergencyFlask(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


//    UUID CAST_TIME_REDUCT_UUID = UUID.fromString("4c3bdc75-9f97-47c7-b391-e2fd2ca9cabf");
//    private static final Object2BooleanOpenHashMap<UUID> WAS_ACTIVE_MAP = new Object2BooleanOpenHashMap<>();
//
//    @Override
//    public void curioTick(SlotContext slotContext, ItemStack stack) {
//        if (slotContext.entity() != null) {
//            LivingEntity livingEntity = slotContext.entity();
//            if (!livingEntity.level().isClientSide() && livingEntity.tickCount % 10 == 0) {
//                AttributeInstance castTime = livingEntity.getAttribute(BrutalityAttributes.CAST_TIME.get());
//                boolean active = livingEntity.getHealth() / livingEntity.getMaxHealth() < 0.5F;
//                UUID uuid = livingEntity.getUUID();
//                boolean wasActive = WAS_ACTIVE_MAP.getOrDefault(uuid, false);
//                if (castTime != null && wasActive != active) {
//                    WAS_ACTIVE_MAP.put(uuid, active);
//                    castTime.removeModifier(CAST_TIME_REDUCT_UUID);
//                    if (active)
//                        castTime.addTransientModifier(
//                                new AttributeModifier(
//                                        CAST_TIME_REDUCT_UUID,
//                                        "Cast Time Buff",
//                                        0.33F,
//                                        AttributeModifier.Operation.MULTIPLY_BASE
//                                )
//                        );
//                }
//            }
//        }
//    }
//    @Override
//    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
//        if (slotContext.entity() != null) WAS_ACTIVE_MAP.removeBoolean(slotContext.entity().getUUID());
//    }


    @Override
    public double getDynamicAttributeBonus(LivingEntity owner, ItemStack stack, Attribute attribute, double currentBonus) {
        if (attribute == BrutalityAttributes.CAST_TIME.get()) {
            if (owner.getHealth() / owner.getMaxHealth() <= 0.5F) {
                return -0.33F;
            }
        }
        return super.getDynamicAttributeBonus(owner, stack, attribute, currentBonus);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null && slotContext.entity().level().isClientSide()) {
            if (slotContext.entity().getHealth() / slotContext.entity().getMaxHealth() < 0.5F) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
                builder.put(BrutalityAttributes.CAST_TIME.get(),
                        new AttributeModifier(uuid,
                                "Cast Time Reduction",
                                -0.33F,
                                AttributeModifier.Operation.ADDITION
                        ));
                return builder.build();
            }
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }
}
