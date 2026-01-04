package net.goo.brutality.item.curios.heart;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.goo.brutality.item.curios.base.BaseHeartCurio;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class BrutalHeart extends BaseHeartCurio {

    public BrutalHeart(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID BRUTAL_HEART_CRIT_CHANCE_UUID = UUID.fromString("ef1d5c30-926e-11f0-9a87-325096b39f47");
    UUID BRUTAL_HEART_CRIT_DAMAGE_UUID = UUID.fromString("ef1d5ec4-926e-11f0-b2ee-325096b39f47");
    UUID BRUTAL_HEART_MAX_HEALTH_UUID = UUID.fromString("ef1d5f64-926e-11f0-b2b9-325096b39f47");

    private static final Object2FloatOpenHashMap<UUID> OLD_HEALTH_MAP = new Object2FloatOpenHashMap<>();

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null) {
            LivingEntity livingEntity = slotContext.entity();
            if (!livingEntity.level().isClientSide() && livingEntity.tickCount % 10 == 0) {
                AttributeInstance critChance = livingEntity.getAttribute(ModAttributes.CRITICAL_STRIKE_CHANCE.get());
                UUID uuid = livingEntity.getUUID();

                if (critChance != null) {
                    float newHealth = livingEntity.getMaxHealth();
                    float oldHealth = OLD_HEALTH_MAP.getOrDefault(uuid, 0.0F);
                    if (Math.abs(oldHealth - newHealth) > 0.0001) {
                        float originalHealth = newHealth * (1 / 0.75F);
                        float healthDiff = originalHealth - newHealth;

                        OLD_HEALTH_MAP.put(uuid, newHealth);
                        critChance.removeModifier(BRUTAL_HEART_CRIT_CHANCE_UUID);
                        critChance.addTransientModifier(new AttributeModifier(BRUTAL_HEART_CRIT_CHANCE_UUID, "Crit Chance Buff", healthDiff * 0.05F,
                                AttributeModifier.Operation.MULTIPLY_BASE));
                    }
                }

            }
        }
    }


    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(Attributes.MAX_HEALTH, new AttributeModifier(BRUTAL_HEART_MAX_HEALTH_UUID, "Health Debuff", -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL));
        builder.put(ModAttributes.CRITICAL_STRIKE_DAMAGE.get(), new AttributeModifier(BRUTAL_HEART_CRIT_DAMAGE_UUID, "Crit Damage Buff", 0.25, AttributeModifier.Operation.MULTIPLY_BASE));
        if (slotContext.entity() != null) {
            LivingEntity livingEntity = slotContext.entity();
            // Convert health lost from THIS CURIO at a ratio of 1hp = 5% crit
            // AttributeInstance healthInstance = livingEntity.getAttribute(Attributes.MAX_HEALTH);
            // Easier to just use a 1.25/1 ratio

            float alteredHealth = livingEntity.getMaxHealth();
            float originalHealth = alteredHealth * (1 / 0.75F);
            float healthDiff = originalHealth - alteredHealth;

            builder.put(ModAttributes.CRITICAL_STRIKE_CHANCE.get(), new AttributeModifier(BRUTAL_HEART_CRIT_CHANCE_UUID, "Crit Chance Buff", healthDiff * 0.05, AttributeModifier.Operation.MULTIPLY_BASE));

        }

        return builder.build();
    }

}
