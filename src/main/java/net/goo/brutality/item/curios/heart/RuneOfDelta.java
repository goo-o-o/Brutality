package net.goo.brutality.item.curios.heart;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
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

public class RuneOfDelta extends BrutalityCurioItem {
    public RuneOfDelta(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID RUNE_OF_DELTA_MAX_MANA_UUID = UUID.fromString("0ffa85eb-7b38-4ce6-b63e-b4cc74add07f");
    UUID RUNE_OF_DELTA_MANA_REGEN_UUID = UUID.fromString("fc619072-277b-4cc2-b70d-57a37397005c");

    private static final Object2IntOpenHashMap<UUID> OLD_BONUS_MAP = new Object2IntOpenHashMap<>();

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null) {
            LivingEntity livingEntity = slotContext.entity();
            if (!livingEntity.level().isClientSide() && livingEntity.tickCount % 10 == 0) {
                AttributeInstance manaRegen = livingEntity.getAttribute(BrutalityModAttributes.MANA_REGEN.get());
                int lightLevel = slotContext.entity().level().getMaxLocalRawBrightness(slotContext.entity().getOnPos().above());
                int newBonus = Math.max(0, 7 - lightLevel);
                UUID uuid = livingEntity.getUUID();
                int oldBonus = OLD_BONUS_MAP.getOrDefault(uuid, 0);
                if (manaRegen != null && oldBonus != newBonus) {
                    OLD_BONUS_MAP.put(uuid, newBonus);
                    manaRegen.removeModifier(RUNE_OF_DELTA_MANA_REGEN_UUID);
                    if (newBonus > 0)
                        manaRegen.addTransientModifier(
                                new AttributeModifier(
                                        RUNE_OF_DELTA_MANA_REGEN_UUID,
                                        "Mana Regen Bonus",
                                        newBonus,
                                        AttributeModifier.Operation.ADDITION
                                )
                        );
                }
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() != null) OLD_BONUS_MAP.removeInt(slotContext.entity().getUUID());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(BrutalityModAttributes.MAX_MANA.get(), new AttributeModifier(RUNE_OF_DELTA_MAX_MANA_UUID, "Max Mana Buff", 20, AttributeModifier.Operation.ADDITION));
        if (slotContext.entity() != null) {
            int lightLevel = slotContext.entity().level().getMaxLocalRawBrightness(slotContext.entity().getOnPos().above());
            int bonus = Math.max(0, 7 - lightLevel);
            if (bonus > 0)
                builder.put(BrutalityModAttributes.MANA_REGEN.get(), new AttributeModifier(RUNE_OF_DELTA_MANA_REGEN_UUID, "Mana Regen Buff", bonus, AttributeModifier.Operation.ADDITION));

        }

        return builder.build();
    }
}
