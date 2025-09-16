package net.goo.brutality.item.curios.anklet;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.goo.brutality.item.base.BrutalityAnkletItem;
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

public class AnkletOfTheImprisoned extends BrutalityAnkletItem {


    public AnkletOfTheImprisoned(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID AOTI_CRIT_CHANCE_UUID = UUID.fromString("0ea9e820-3aee-4977-8b7d-147cba7af3f6");
    UUID AOTI_CRIT_DAMAGE_UUID = UUID.fromString("30376e02-d415-4f55-9ea0-8f8339f83cc6");
    private static final Object2FloatOpenHashMap<UUID> OLD_HEALTH_MAP = new Object2FloatOpenHashMap<>();

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null) {
            LivingEntity livingEntity = slotContext.entity();
            if (!livingEntity.level().isClientSide() && livingEntity.tickCount % 10 == 0) {
                AttributeInstance critChance = livingEntity.getAttribute(ModAttributes.CRITICAL_STRIKE_CHANCE.get());
                AttributeInstance critDamage = livingEntity.getAttribute(ModAttributes.CRITICAL_STRIKE_DAMAGE.get());
                UUID uuid = livingEntity.getUUID();

                if (critChance != null & critDamage != null) {
                    float newHealth = livingEntity.getHealth();
                    float oldHealth = OLD_HEALTH_MAP.getOrDefault(uuid, 0.0F);
                    if (Math.abs(oldHealth - newHealth) > 0.0001) {
                        float missingHealth = livingEntity.getMaxHealth() - newHealth;
                        float newBonus = missingHealth * 0.075F;
                        OLD_HEALTH_MAP.put(uuid, newHealth);
                        critDamage.removeModifier(AOTI_CRIT_DAMAGE_UUID);
                        critChance.removeModifier(AOTI_CRIT_CHANCE_UUID);

                        critDamage.addTransientModifier(new AttributeModifier(AOTI_CRIT_DAMAGE_UUID, "Crit Damage Buff", newBonus,
                                AttributeModifier.Operation.MULTIPLY_BASE));
                        critChance.addTransientModifier(new AttributeModifier(AOTI_CRIT_CHANCE_UUID, "Crit Chance Buff", newBonus,
                                AttributeModifier.Operation.MULTIPLY_BASE));
                    }
                }

            }

            if (livingEntity.tickCount % 20 == 0) {
                slotContext.entity().hurt(slotContext.entity().damageSources().generic(), 1);
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() != null) OLD_HEALTH_MAP.removeFloat(slotContext.entity().getUUID());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            LivingEntity livingEntity = slotContext.entity();
            float missingHealth = livingEntity.getMaxHealth() - livingEntity.getHealth();
            float newBonus = missingHealth * 0.075F;

            builder.put(ModAttributes.CRITICAL_STRIKE_CHANCE.get(),
                    new AttributeModifier(AOTI_CRIT_CHANCE_UUID, "Crit Chance Buff", newBonus, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(ModAttributes.CRITICAL_STRIKE_DAMAGE.get(),
                    new AttributeModifier(AOTI_CRIT_DAMAGE_UUID, "Crit Damage Buff", newBonus, AttributeModifier.Operation.MULTIPLY_BASE));
            return builder.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }

}
