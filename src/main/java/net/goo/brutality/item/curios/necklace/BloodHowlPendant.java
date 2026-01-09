package net.goo.brutality.item.curios.necklace;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.goo.brutality.item.curios.BrutalityCurioItem;
import net.goo.brutality.registry.BrutalityModAttributes;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class BloodHowlPendant extends BrutalityCurioItem {


    public BloodHowlPendant(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID BLOOD_HOWL_RAGE_GAIN_UUID = UUID.fromString("9f300d11-6844-4a87-8350-eb0cee580300");
    private static final Object2BooleanOpenHashMap<UUID> WAS_ACTIVE_MAP = new Object2BooleanOpenHashMap<>();


    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null) {
            LivingEntity livingEntity = slotContext.entity();
            if (!livingEntity.level().isClientSide() && livingEntity.tickCount % 10 == 0) {
                AttributeInstance rageGain = livingEntity.getAttribute(BrutalityModAttributes.DAMAGE_TO_RAGE_RATIO.get());
                boolean active = livingEntity.getHealth() / livingEntity.getMaxHealth() < 0.5F;
                UUID uuid = livingEntity.getUUID();
                boolean wasActive = WAS_ACTIVE_MAP.getOrDefault(uuid, false);
                if (rageGain != null && wasActive != active) {
                    WAS_ACTIVE_MAP.put(uuid, active);
                    rageGain.removeModifier(BLOOD_HOWL_RAGE_GAIN_UUID);
                    if (active)
                        rageGain.addTransientModifier(
                                new AttributeModifier(
                                        BLOOD_HOWL_RAGE_GAIN_UUID,
                                        "Rage Gain Buff",
                                        0.5F,
                                        AttributeModifier.Operation.MULTIPLY_TOTAL
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
                builder.put(BrutalityModAttributes.DAMAGE_TO_RAGE_RATIO.get(),
                        new AttributeModifier(BLOOD_HOWL_RAGE_GAIN_UUID,
                                "Rage Gain Buff",
                                0.5,
                                AttributeModifier.Operation.MULTIPLY_TOTAL
                        ));
                return builder.build();
            }
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }
}
