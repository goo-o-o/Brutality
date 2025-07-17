package net.goo.brutality.item.curios;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class AbyssalNecklaceNecklace extends BrutalityCurioItem {
    public AbyssalNecklaceNecklace(Properties pProperties, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pProperties, identifier, rarity, descriptionComponents);
    }

    private static float getCurrentBonus() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && player.isInWaterRainOrBubble()) {
            return (float) (10F - (((Minecraft.getInstance().player.getY() + 64F) / 127F) * 10F));
        } else return 0;
    }

    UUID ABYSS_NECKLACE_AD_UUID = UUID.fromString("de956292-15b7-432d-b5e6-55ca6be388ac");

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            AttributeInstance attackDamage = player.getAttribute(Attributes.ATTACK_DAMAGE);
            if (attackDamage != null) {
                // Remove old modifier (if exists)
                attackDamage.removeModifier(ABYSS_NECKLACE_AD_UUID);

                // Add new modifier with dynamic value
                attackDamage.addTransientModifier(
                        new AttributeModifier(
                                ABYSS_NECKLACE_AD_UUID,
                                "Depth AD Bonus",
                                getCurrentBonus(),
                                AttributeModifier.Operation.ADDITION
                        )
                );
            }
        }
    }

    UUID ABYSS_NECKLACE_SWIM_SPEED_UUID = UUID.fromString("8e16861a-63a6-4444-9252-faf7211f6745");

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("curios.modifiers.charm").withStyle(ChatFormatting.GOLD));
        float value = getCurrentBonus();

        String formattedValue = value % 1 == 0 ?
                String.format("%.0f", value) :
                String.format("%.1f", value);

        tooltip.add(Component.literal((value > 0 ? "+" : "") + formattedValue + " ").append(Component.translatable("attribute.name.generic.attack_damage"))
                .withStyle(value > 0 ? ChatFormatting.BLUE : ChatFormatting.RED));

    }


    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(ForgeMod.SWIM_SPEED.get(), new AttributeModifier(ABYSS_NECKLACE_SWIM_SPEED_UUID, "MS Buff", 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL));
            return builder.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }
}
