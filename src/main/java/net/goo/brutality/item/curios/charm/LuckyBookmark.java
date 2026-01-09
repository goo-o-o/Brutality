package net.goo.brutality.item.curios.charm;

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
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class LuckyBookmark extends BrutalityCurioItem {

    public LuckyBookmark(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID MANA_REGEN = UUID.fromString("29403525-01f2-4095-a5a3-a37583f84852");

    private static final Object2FloatOpenHashMap<UUID> OLD_BONUS_MAP = new Object2FloatOpenHashMap<>();
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null) {
            LivingEntity livingEntity = slotContext.entity();
            if (!livingEntity.level().isClientSide() && livingEntity.tickCount % 10 == 0) {
                AttributeInstance manaRegen = livingEntity.getAttribute(BrutalityModAttributes.MANA_REGEN.get());
                float newBonus = (float) (livingEntity.getAttributeValue(Attributes.LUCK) * 0.2F);
                UUID uuid = livingEntity.getUUID();
                float oldBonus = OLD_BONUS_MAP.getOrDefault(uuid, 0.0F);
                if (manaRegen != null && Math.abs(oldBonus - newBonus) > 0.0001) {
                    OLD_BONUS_MAP.put(uuid, newBonus);
                    manaRegen.removeModifier(MANA_REGEN);
                    manaRegen.addTransientModifier(
                            new AttributeModifier(
                                    MANA_REGEN,
                                    "Mana Regen Bonus",
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
            builder.put(BrutalityModAttributes.MANA_REGEN.get(), new AttributeModifier(MANA_REGEN, "Mana Regen Buff",
                    slotContext.entity().getAttributeValue(Attributes.LUCK) * 0.25F, AttributeModifier.Operation.MULTIPLY_BASE));
            return builder.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }


}
