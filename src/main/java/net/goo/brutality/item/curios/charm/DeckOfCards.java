package net.goo.brutality.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.curios.base.BaseCharmCurio;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class DeckOfCards extends BaseCharmCurio {


    public DeckOfCards(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);

    }

    UUID DECK_OF_CARDS_SPELL_DAMAGE_UUID = UUID.fromString("f22b0f7c-2446-4f4b-9c00-1d8e1d384542");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ModAttributes.SPELL_DAMAGE.get(),
                new AttributeModifier(DECK_OF_CARDS_SPELL_DAMAGE_UUID, "Spell Damage Buff", 0.13, AttributeModifier.Operation.MULTIPLY_BASE));

        return builder.build();

    }
}
