package net.goo.brutality.item.curios.head;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.curios.base.BaseHeadCurio;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import software.bernie.geckolib.core.animation.AnimatableManager;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class SanguineSpectacles extends BaseHeadCurio {
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    public SanguineSpectacles(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID SANGUINE_SPECTACLES_OMNIVAMP_UUID = UUID.fromString("e84df0b9-be7e-4d65-b138-77e746cec5bc");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ModAttributes.OMNIVAMP.get(), new AttributeModifier(SANGUINE_SPECTACLES_OMNIVAMP_UUID,
                "Omnivamp Boost", 0.025, AttributeModifier.Operation.MULTIPLY_BASE));
        return builder.build();
    }

}
