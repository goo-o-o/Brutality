package net.goo.brutality.item.curios.hands;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.curios.base.BaseHandsCurio;
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

public class Dumbbell extends BaseHandsCurio {


    public Dumbbell(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID DUMBBELL_BLUNT_DMG = UUID.fromString("b7454313-551e-4e5e-93e5-b6a67a90a1f3");
    UUID DUMBBELL_MS = UUID.fromString("998382bd-d5c8-4ad6-8b88-5d570ed4239f");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ModAttributes.BLUNT_DAMAGE.get(),
                new AttributeModifier(DUMBBELL_BLUNT_DMG, "Blunt Damage Buff", 0.15, AttributeModifier.Operation.MULTIPLY_TOTAL));
        builder.put(Attributes.MOVEMENT_SPEED,
                new AttributeModifier(DUMBBELL_MS, "Speed Nerf", -0.05, AttributeModifier.Operation.MULTIPLY_TOTAL));

        return builder.build();

    }
}
