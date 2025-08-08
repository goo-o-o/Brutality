package net.goo.brutality.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class Wrath extends BrutalityCurioItem {


    public Wrath(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }

//    private static float getCurrentBonus() {
//        LocalPlayer player = Minecraft.getInstance().player;
//        if (player != null) {
//            return (1 - player.getHealth() / player.getMaxHealth()) * 40;
//        } else return 0;
//    }

    UUID WRATH_RAGE_UUID = UUID.fromString("2d029ff6-aae3-424f-950e-5cd68a96d6c0");
    UUID WRATH_RAGE_TIME_UUID = UUID.fromString("e6f8b7ec-d379-403d-8cb6-7e51a15dca09");
    UUID WRATH_RAGE_GAIN_UUID = UUID.fromString("7d91e73a-6d7a-4576-8dfe-845898a6c63b");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ModAttributes.MAX_RAGE.get(), new AttributeModifier(WRATH_RAGE_UUID, "Max Rage Buff", 50, AttributeModifier.Operation.ADDITION));
            builder.put(ModAttributes.RAGE_TIME_MULTIPLIER.get(), new AttributeModifier(WRATH_RAGE_TIME_UUID, "Max Rage Time Buff", 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL));
            builder.put(ModAttributes.RAGE_GAIN_MULTIPLIER.get(), new AttributeModifier(WRATH_RAGE_GAIN_UUID, "Rage Gain Buff", 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL));
            return builder.build();

        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            AttributeInstance maxRage = player.getAttribute(ModAttributes.MAX_RAGE.get());
            if (maxRage != null) {
                maxRage.removeModifier(WRATH_RAGE_UUID);
            }
            AttributeInstance rageTime = player.getAttribute(ModAttributes.RAGE_TIME_MULTIPLIER.get());
            if (rageTime != null) {
                rageTime.removeModifier(WRATH_RAGE_TIME_UUID);
            }
            AttributeInstance rageGain = player.getAttribute(ModAttributes.RAGE_GAIN_MULTIPLIER.get());
            if (rageGain != null) {
                rageGain.removeModifier(WRATH_RAGE_GAIN_UUID);
            }
        }
    }

//    @Override
//    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
//        super.appendHoverText(stack, world, tooltip, flag);
//        tooltip.add(Component.empty());
//        float value = getCurrentBonus();
//
//        String formattedValue = value % 1 == 0 ?
//                String.format("%.0f", value) :
//                String.format("%.1f", value);
//
//        tooltip.add(Component.literal((value >= 0 ? "+" : "") + formattedValue + "% ").append(Component.translatable("attribute.name.generic.attack_damage"))
//                .withStyle(value >= 0 ? ChatFormatting.BLUE : ChatFormatting.RED));
//
//    }

}
