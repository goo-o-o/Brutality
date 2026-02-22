package net.goo.brutality.common.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.curios.BrutalityMathFunctionCurio;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class Cosine extends BrutalityMathFunctionCurio {


    public Cosine(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    public static float getCurrentBonus(Level level) {
        // 1. Define constants as double for maximum precision during calculation
        double frequency = 0.025;
        double period = (Math.PI * 2) / frequency;

        // 2. Perform the modulo on the raw long/double time
        // This keeps 'remainder' small (between 0 and ~251.32)
        double remainder = level.getGameTime() % period;

        // 3. Now that the number is small, casting to float is safe
        return (float) Math.cos(remainder * frequency) * 0.175F + 0.075F; // -10% to 25%
    }

    @Override
    public double getDynamicAttributeBonus(LivingEntity owner, ItemStack stack, Attribute attribute, double currentBonus) {
        if (attribute == Attributes.ATTACK_SPEED) {
            return getCurrentBonus(owner.level()) * currentBonus;
        }
        return super.getDynamicAttributeBonus(owner, stack, attribute, currentBonus);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {

        if (slotContext.entity() != null && slotContext.entity().level().isClientSide()) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

            Attribute attribute = Attributes.ATTACK_SPEED;

            AttributeModifier modifier = new AttributeModifier(uuid, String.format("%s_%s_modifier", Brutality.MOD_ID, attribute.getDescriptionId()),
                    getCurrentBonus(slotContext.entity().level()), AttributeModifier.Operation.MULTIPLY_TOTAL);
            builder.put(attribute, modifier);
            return builder.build();
        }

        return super.getAttributeModifiers(slotContext, uuid, stack);
    }

//    @Override
//    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
//        super.appendHoverText(stack, world, tooltip, flag);
//        tooltip.add(Component.empty());
//        tooltip.add(Component.translatable("curios.modifiers.charm").withStyle(ChatFormatting.GOLD));
//
//        float value = getCurrentBonus(world) * 100;
//
//        String formattedValue = value % 1 == 0 ?
//                String.format("%.0f", value) :
//                String.format("%.1f", value);
//
//        tooltip.add(Component.literal((value >= 0 ? "+" : "") + formattedValue + "% ").append(Component.translatable("attribute.name.generic.attack_speed"))
//                .withStyle(value >= 0 ? ChatFormatting.BLUE : ChatFormatting.RED));
//
//    }
}
