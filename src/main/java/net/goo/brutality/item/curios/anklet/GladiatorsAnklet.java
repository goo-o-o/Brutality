package net.goo.brutality.item.curios.anklet;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityAnkletItem;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class GladiatorsAnklet extends BrutalityAnkletItem {


    public GladiatorsAnklet(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.ANKLET;
    }

    UUID GLADIATORS_ANKLET_DODGE_UUID = UUID.fromString("8e87e841-6987-4e4d-8331-fc74f4eb401c");
    UUID GLADIATORS_ANKLET_SLASH_UUID = UUID.fromString("55401e46-2eee-48d9-ae04-f6ed8a734f2c");
    UUID GLADIATORS_ANKLET_PIERCING_UUID = UUID.fromString("caa9fb62-9f17-4444-b90a-956d1c123404");
    UUID GLADIATORS_ANKLET_BLUNT_UUID = UUID.fromString("082fd360-0dca-482b-ba14-79a16a7848eb");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ModAttributes.DODGE_CHANCE.get(),
                    new AttributeModifier(GLADIATORS_ANKLET_DODGE_UUID, "Dodge Buff", 0.05, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(ModAttributes.SLASH_DAMAGE.get(),
                    new AttributeModifier(GLADIATORS_ANKLET_SLASH_UUID, "Slash Damage",  3.5F, AttributeModifier.Operation.ADDITION));
            builder.put(ModAttributes.PIERCING_DAMAGE.get(),
                    new AttributeModifier(GLADIATORS_ANKLET_PIERCING_UUID, "Piercing Damage", 2.5F, AttributeModifier.Operation.ADDITION));
            builder.put(ModAttributes.BLUNT_DAMAGE.get(),
                    new AttributeModifier(GLADIATORS_ANKLET_BLUNT_UUID, "Blunt Damage", -4F, AttributeModifier.Operation.ADDITION));
            return builder.build();

        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }
}
