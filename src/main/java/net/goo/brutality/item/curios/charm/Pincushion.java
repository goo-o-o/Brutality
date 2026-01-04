package net.goo.brutality.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.goo.brutality.item.curios.base.BaseCharmCurio;
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

public class Pincushion extends BaseCharmCurio {
    public Pincushion(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID PINCUSHION_LETAHALITY_UUID = UUID.fromString("8994a240-2339-41bd-83aa-722ceb90040f");

    private static final Object2IntOpenHashMap<UUID> OLD_ARROW_COUNT_MAP = new Object2IntOpenHashMap<>();

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null) {
            LivingEntity livingEntity = slotContext.entity();
            if (!livingEntity.level().isClientSide() && livingEntity.tickCount % 10 == 0) {
                AttributeInstance lethality = livingEntity.getAttribute(ModAttributes.LETHALITY.get());
                UUID uuid = livingEntity.getUUID();

                if (lethality != null) {
                    int newArrowCount = livingEntity.getArrowCount();
                    int oldArrowCount = OLD_ARROW_COUNT_MAP.getOrDefault(uuid, 0);
                    if (newArrowCount != oldArrowCount) {
                        OLD_ARROW_COUNT_MAP.put(uuid, newArrowCount);
                        lethality.removeModifier(PINCUSHION_LETAHALITY_UUID);

                        lethality.addTransientModifier(new AttributeModifier(PINCUSHION_LETAHALITY_UUID, "Crit Chance Buff", newArrowCount,
                                AttributeModifier.Operation.ADDITION));
                    }
                }

            }
        }
    }


    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            int arrowCount = slotContext.entity().getArrowCount();
            builder.put(ModAttributes.LETHALITY.get(), new AttributeModifier(PINCUSHION_LETAHALITY_UUID, "Lethality Buff", arrowCount, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }
}
