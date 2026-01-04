package net.goo.brutality.item.curios.head;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.curios.base.BaseHeadCurio;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import software.bernie.geckolib.core.animation.AnimatableManager;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class ProgenitorsEarrings extends BaseHeadCurio {

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    public ProgenitorsEarrings(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID PROGENITORS_EARRINGS_LIFESTEAL_UUID = UUID.fromString("5da40935-1e7f-4c78-96ae-9cafeb811735");
    UUID PROGENITORS_EARRINGS_ARMOR_UUID = UUID.fromString("593b1093-3d9e-4347-ba07-08063482fd03");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ModAttributes.LIFESTEAL.get(), new AttributeModifier(PROGENITORS_EARRINGS_LIFESTEAL_UUID,
                "Lifesteal Boost", 0.25, AttributeModifier.Operation.MULTIPLY_BASE));
        builder.put(Attributes.ARMOR, new AttributeModifier(PROGENITORS_EARRINGS_ARMOR_UUID,
                "Armor Nerf", -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL));
        return builder.build();
    }

}
