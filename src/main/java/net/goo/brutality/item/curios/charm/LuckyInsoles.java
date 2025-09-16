package net.goo.brutality.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
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

public class LuckyInsoles extends BrutalityCurioItem {
    public LuckyInsoles(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }

    private static final Object2BooleanOpenHashMap<UUID> WAS_ACTIVE_MAP = new Object2BooleanOpenHashMap<>();

    UUID LUCKY_INSOLES_CRIT_CHANCE_UUID = UUID.fromString("beb2cc24-92ff-11f0-9848-325096b39f47");


    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null) {
            LivingEntity livingEntity = slotContext.entity();
            if (!livingEntity.level().isClientSide() && livingEntity.tickCount % 10 == 0) {
                AttributeInstance critChance = livingEntity.getAttribute(ModAttributes.CRITICAL_STRIKE_CHANCE.get());
                boolean active = livingEntity.onGround();
                UUID uuid = livingEntity.getUUID();
                boolean wasActive = WAS_ACTIVE_MAP.getOrDefault(uuid, false);
                if (critChance != null && wasActive != active) {
                    WAS_ACTIVE_MAP.put(uuid, active);
                    critChance.removeModifier(LUCKY_INSOLES_CRIT_CHANCE_UUID);
                    if (active)
                        critChance.addTransientModifier(
                                new AttributeModifier(
                                        LUCKY_INSOLES_CRIT_CHANCE_UUID,
                                        "Crit Chance Buff",
                                        0.2F,
                                        AttributeModifier.Operation.MULTIPLY_BASE
                                )
                        );
                }
            }
        }
    }


    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        if (slotContext.entity() != null) {
            LivingEntity livingEntity = slotContext.entity();

            if (livingEntity.onGround()) {
                builder.put(ModAttributes.CRITICAL_STRIKE_CHANCE.get(), new AttributeModifier(LUCKY_INSOLES_CRIT_CHANCE_UUID, "Crit Chance Buff",0.2F, AttributeModifier.Operation.MULTIPLY_BASE));
            }

        }

        return builder.build();
    }
}
