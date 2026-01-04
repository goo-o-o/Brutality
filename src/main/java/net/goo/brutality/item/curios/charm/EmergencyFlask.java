package net.goo.brutality.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.goo.brutality.item.curios.base.BaseCharmCurio;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class EmergencyFlask extends BaseCharmCurio {


    public EmergencyFlask(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


    UUID CAST_TIME_REDUCT_UUID = UUID.fromString("4c3bdc75-9f97-47c7-b391-e2fd2ca9cabf");
    private static final Object2BooleanOpenHashMap<UUID> WAS_ACTIVE_MAP = new Object2BooleanOpenHashMap<>();

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null) {
            LivingEntity livingEntity = slotContext.entity();
            if (!livingEntity.level().isClientSide() && livingEntity.tickCount % 10 == 0) {
                AttributeInstance castTime = livingEntity.getAttribute(ModAttributes.CAST_TIME_REDUCTION.get());
                boolean active = livingEntity.getHealth() / livingEntity.getMaxHealth() < 0.5F;
                UUID uuid = livingEntity.getUUID();
                boolean wasActive = WAS_ACTIVE_MAP.getOrDefault(uuid, false);
                if (castTime != null && wasActive != active) {
                    WAS_ACTIVE_MAP.put(uuid, active);
                    castTime.removeModifier(CAST_TIME_REDUCT_UUID);
                    if (active)
                        castTime.addTransientModifier(
                                new AttributeModifier(
                                        CAST_TIME_REDUCT_UUID,
                                        "Cast Time Buff",
                                        0.33F,
                                        AttributeModifier.Operation.MULTIPLY_BASE
                                )
                        );
                }
            }
        }
    }
    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() != null) WAS_ACTIVE_MAP.removeBoolean(slotContext.entity().getUUID());
    }


    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            if (slotContext.entity().getHealth() / slotContext.entity().getMaxHealth() < 0.5F) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
                builder.put(ModAttributes.CAST_TIME_REDUCTION.get(),
                        new AttributeModifier(CAST_TIME_REDUCT_UUID,
                                "Cast Time Bonus",
                                0.33F,
                                AttributeModifier.Operation.MULTIPLY_BASE
                        ));
                return builder.build();
            }
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }
}
