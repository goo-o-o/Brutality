package net.goo.brutality.item.curios.hands;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.goo.brutality.item.curios.BrutalityCurioItem;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
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

public class SuspiciouslyLargeHandle extends BrutalityCurioItem {
    private static final float BASE_SPEED = 0.65F;

    public SuspiciouslyLargeHandle(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    private static final UUID ATTACK_DAMAGE_UUID = UUID.fromString("a48b7605-944a-4603-8cb8-a30ef6ea12cc");
    private static final UUID ATTACK_SPEED_UUID = UUID.fromString("65363f53-3746-4048-b1c2-32f76f750b97");

    private static final Object2FloatOpenHashMap<UUID> LAST_DAMAGE = new Object2FloatOpenHashMap<>();
    private static final Object2FloatOpenHashMap<UUID> LAST_SPEED = new Object2FloatOpenHashMap<>();

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (entity == null || entity.level().isClientSide() || entity.tickCount % 10 != 0) return;

        UUID uuid = entity.getUUID();
        float attackSpeed = (float) entity.getAttributeValue(Attributes.ATTACK_SPEED);
        AttributeInstance attackSpeedAttr = entity.getAttribute(Attributes.ATTACK_SPEED);
        float handleAttackSpeed = 0;
        if (attackSpeedAttr != null && attackSpeedAttr.getModifier(ATTACK_SPEED_UUID) != null) {
            handleAttackSpeed = (float) attackSpeedAttr.getModifier(ATTACK_SPEED_UUID).getAmount();
        }
        attackSpeed -= handleAttackSpeed;

        // This could be negative, negative means the player's attack speed is increased instead. So we need to nerf their damage instead of boost
        float difference = BASE_SPEED - attackSpeed;
        // 0.01 AS = 0.05 AD, 5x ratio
        float damageBoost = difference * 5F;

        // Damage modifier
        float oldDamage = LAST_DAMAGE.getOrDefault(uuid, Float.NaN);
        if (Math.abs(oldDamage - damageBoost) > 0.0001F) {
            AttributeInstance dmg = entity.getAttribute(Attributes.ATTACK_DAMAGE);
            if (dmg != null) {
                dmg.removeModifier(ATTACK_DAMAGE_UUID);
                if (Math.abs(damageBoost) > 0.0001F) {
                    dmg.addTransientModifier(new AttributeModifier(ATTACK_DAMAGE_UUID, "Large Handle Damage Boost", damageBoost, AttributeModifier.Operation.ADDITION));
                }
                LAST_DAMAGE.put(uuid, damageBoost);
            }
        }

        // Speed modifier (mirrors difference)
        float oldSpeed = LAST_SPEED.getOrDefault(uuid, Float.NaN);
        if (Math.abs(oldSpeed - difference) > 0.0001F) {
            AttributeInstance speed = entity.getAttribute(Attributes.ATTACK_SPEED);
            if (speed != null) {
                speed.removeModifier(ATTACK_SPEED_UUID);
                if (Math.abs(difference) > 0.0001F) {
                    speed.addTransientModifier(new AttributeModifier(ATTACK_SPEED_UUID, "Large Handle Speed Nerf", difference, AttributeModifier.Operation.ADDITION));
                }
                LAST_SPEED.put(uuid, difference);
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (entity != null && !entity.level().isClientSide()) {
            UUID uuid = entity.getUUID();
            LAST_DAMAGE.removeFloat(uuid);
            LAST_SPEED.removeFloat(uuid);
        }
    }


    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            LivingEntity entity = slotContext.entity();
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();

            float attackSpeed = (float) entity.getAttributeValue(Attributes.ATTACK_SPEED);
            AttributeInstance attackSpeedAttr = entity.getAttribute(Attributes.ATTACK_SPEED);
            float handleAttackSpeed = 0;
            if (attackSpeedAttr != null && attackSpeedAttr.getModifier(ATTACK_SPEED_UUID) != null) {
                handleAttackSpeed = (float) attackSpeedAttr.getModifier(ATTACK_SPEED_UUID).getAmount();
            }
            attackSpeed -= handleAttackSpeed;

            // This could be negative, negative means the player's attack speed is increased instead. So we need to nerf their damage instead of boost
            float difference = BASE_SPEED - attackSpeed;
            // 0.01 AS = 0.05 AD, 5x ratio
            float damageBoost = difference * 5F;


            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_UUID, "Attack Damage Buff", damageBoost, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_UUID, "Attack Speed Nerf", difference, AttributeModifier.Operation.ADDITION));
            return builder.build();

        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }
}
