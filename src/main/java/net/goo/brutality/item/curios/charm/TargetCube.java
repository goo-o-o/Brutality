package net.goo.brutality.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
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

public class TargetCube extends BrutalityCurioItem {

    public TargetCube(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }

    UUID TARGET_CUBE_CRIT_CHANCE_UUID = UUID.fromString("fd88bba5-e451-4ed7-aa8a-b3f19e14a5ee");

    private static final Object2FloatOpenHashMap<UUID> OLD_BONUS_MAP = new Object2FloatOpenHashMap<>();
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null) {
            LivingEntity livingEntity = slotContext.entity();
            if (!livingEntity.level().isClientSide() && livingEntity.tickCount % 10 == 0) {
                AttributeInstance attackDamage = livingEntity.getAttribute(ModAttributes.CRITICAL_STRIKE_CHANCE.get());
                float newBonus = (float) (livingEntity.getAttributeValue(Attributes.LUCK) * 0.1F);
                UUID uuid = livingEntity.getUUID();
                float oldBonus = OLD_BONUS_MAP.getOrDefault(uuid, 0.0F);
                if (attackDamage != null && Math.abs(oldBonus - newBonus) > 0.0001) {
                    OLD_BONUS_MAP.put(uuid, newBonus);
                    attackDamage.removeModifier(TARGET_CUBE_CRIT_CHANCE_UUID);
                    attackDamage.addTransientModifier(
                            new AttributeModifier(
                                    TARGET_CUBE_CRIT_CHANCE_UUID,
                                    "Crit Chance Bonus",
                                    newBonus,
                                    AttributeModifier.Operation.MULTIPLY_BASE
                            )
                    );
                }
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() != null) OLD_BONUS_MAP.removeFloat(slotContext.entity().getUUID());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ModAttributes.CRITICAL_STRIKE_CHANCE.get(), new AttributeModifier(TARGET_CUBE_CRIT_CHANCE_UUID, "Crit Chance Buff",
                    slotContext.entity().getAttributeValue(Attributes.LUCK) * 0.1F, AttributeModifier.Operation.MULTIPLY_BASE));
            return builder.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }


}
