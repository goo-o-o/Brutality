package net.goo.brutality.item.curios.necklace;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
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

public class BlackMatterNecklace extends BrutalityCurioItem {


    public BlackMatterNecklace(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID BLACK_MATTER_NECKLACE_CRIT_CHANCE = UUID.fromString("c224324e-92fa-11f0-a121-325096b39f47");
    UUID BLACK_MATTER_NECKLACE_CRIT_DAMAGE = UUID.fromString("c22434d8-92fa-11f0-9246-325096b39f47");
    private static final Object2FloatOpenHashMap<UUID> OLD_ARMOR_MAP = new Object2FloatOpenHashMap<>();

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null) {
            LivingEntity livingEntity = slotContext.entity();
            if (!livingEntity.level().isClientSide() && livingEntity.tickCount % 10 == 0) {
                AttributeInstance critChance = livingEntity.getAttribute(BrutalityModAttributes.CRITICAL_STRIKE_CHANCE.get());
                AttributeInstance critDamage = livingEntity.getAttribute(BrutalityModAttributes.CRITICAL_STRIKE_DAMAGE.get());
                UUID uuid = livingEntity.getUUID();

                if (critChance != null & critDamage != null) {
                    float newArmor = livingEntity.getArmorValue();
                    float oldArmor = OLD_ARMOR_MAP.getOrDefault(uuid, 0.0F);
                    if (Math.abs(oldArmor - newArmor) > 0.0001) {
                        OLD_ARMOR_MAP.put(uuid, newArmor);
                        critDamage.removeModifier(BLACK_MATTER_NECKLACE_CRIT_DAMAGE);
                        critChance.removeModifier(BLACK_MATTER_NECKLACE_CRIT_CHANCE);

                        critChance.addTransientModifier(new AttributeModifier(BLACK_MATTER_NECKLACE_CRIT_CHANCE, "Crit Chance Buff", newArmor * 0.02F,
                                AttributeModifier.Operation.MULTIPLY_BASE));
                        critDamage.addTransientModifier(new AttributeModifier(BLACK_MATTER_NECKLACE_CRIT_DAMAGE, "Crit Damage Buff", newArmor * -0.01F,
                                AttributeModifier.Operation.MULTIPLY_BASE));
                    }
                }

            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() != null) OLD_ARMOR_MAP.removeFloat(slotContext.entity().getUUID());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        LivingEntity livingEntity = slotContext.entity();
        if (livingEntity == null) return super.getAttributeModifiers(slotContext, uuid, stack);
        float armor = livingEntity.getArmorValue();

        builder.put(BrutalityModAttributes.CRITICAL_STRIKE_CHANCE.get(),
                new AttributeModifier(BLACK_MATTER_NECKLACE_CRIT_CHANCE, "Crit Chance Buff", armor * 0.02F, AttributeModifier.Operation.MULTIPLY_BASE));
        builder.put(BrutalityModAttributes.CRITICAL_STRIKE_DAMAGE.get(),
                new AttributeModifier(BLACK_MATTER_NECKLACE_CRIT_DAMAGE, "Crit Damage Buff", armor * -0.01F, AttributeModifier.Operation.MULTIPLY_BASE));
        return builder.build();
    }

}
