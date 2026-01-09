package net.goo.brutality.item.curios.necklace;

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
import net.minecraftforge.common.ForgeMod;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class AbyssalNecklace extends BrutalityCurioItem {

    public AbyssalNecklace(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


    UUID ABYSS_NECKLACE_AD_UUID = UUID.fromString("de956292-15b7-432d-b5e6-55ca6be388ac");
    private static final Object2FloatOpenHashMap<UUID> OLD_BONUS_MAP = new Object2FloatOpenHashMap<>();

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null) {
            LivingEntity livingEntity = slotContext.entity();
            if (!livingEntity.level().isClientSide() && livingEntity.tickCount % 10 == 0) {
                AttributeInstance attackDamage = livingEntity.getAttribute(Attributes.ATTACK_DAMAGE);
                UUID uuid = livingEntity.getUUID();
                if (attackDamage != null) {
                    if (livingEntity.isInWaterRainOrBubble()) {
                        float newBonus = Math.max((float) (5F - ((livingEntity.getY() + 64F) / 384F) * 5F), 0);
                        float oldBonus = OLD_BONUS_MAP.getOrDefault(uuid, 0.0F);

                        if (Math.abs(oldBonus - newBonus) > 0.0001) {
                            OLD_BONUS_MAP.put(uuid, newBonus);
                            attackDamage.removeModifier(ABYSS_NECKLACE_AD_UUID);
                            attackDamage.addTransientModifier(
                                    new AttributeModifier(
                                            ABYSS_NECKLACE_AD_UUID,
                                            "Depth AD Bonus",
                                            newBonus,
                                            AttributeModifier.Operation.ADDITION
                                    )
                            );
                        }
                    } else {
                        attackDamage.removeModifier(ABYSS_NECKLACE_AD_UUID);
                        OLD_BONUS_MAP.removeFloat(uuid);
                    }
                }
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() != null) OLD_BONUS_MAP.removeFloat(slotContext.entity().getUUID());
    }

    UUID ABYSS_NECKLACE_SWIM_SPEED_UUID = UUID.fromString("8e16861a-63a6-4444-9252-faf7211f6745");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ForgeMod.SWIM_SPEED.get(), new AttributeModifier(ABYSS_NECKLACE_SWIM_SPEED_UUID, "MS Buff", 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL));
        if (slotContext.entity() != null && slotContext.entity().isInWaterRainOrBubble()) {
            builder.put(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(
                            ABYSS_NECKLACE_AD_UUID,
                            "Depth AD Bonus",
                            Math.max((float) (5F - ((slotContext.entity().getY() + 64F) / 384F) * 5F), 0),

                            AttributeModifier.Operation.ADDITION
                    ));
        }
        return builder.build();
    }

}
