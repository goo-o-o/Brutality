package net.goo.brutality.item.curios.belt;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class BruteskinBelt extends BrutalityCurioItem {


    public BruteskinBelt(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.BELT;
    }

    UUID BRUTESKIN_BELT_MAX_HEALTH_UUID = UUID.fromString("5221dbb5-e073-45a7-a3a4-6ca256c391e6");
    UUID BRUTESKIN_BELT_CRIT_DAMAGE_UUID = UUID.fromString("8541da84-1904-4785-a719-80c2eb1bf423");
    UUID BRUTESKIN_BELT_CRIT_CHANCE_UUID = UUID.fromString("44ce67bb-6c49-45ca-929e-edc1e5678c3d");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(Attributes.MAX_HEALTH,
                new AttributeModifier(BRUTESKIN_BELT_MAX_HEALTH_UUID, "Health Buff", -0.5, AttributeModifier.Operation.MULTIPLY_TOTAL));
        builder.put(ModAttributes.CRITICAL_STRIKE_DAMAGE.get(),
                new AttributeModifier(BRUTESKIN_BELT_CRIT_DAMAGE_UUID, "Crit Damage Buff", 0.5, AttributeModifier.Operation.MULTIPLY_BASE));
        builder.put(ModAttributes.CRITICAL_STRIKE_CHANCE.get(),
                new AttributeModifier(BRUTESKIN_BELT_CRIT_CHANCE_UUID, "Crit Chance Buff", 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
        return builder.build();

    }
}
