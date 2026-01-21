package net.goo.brutality.item.curios.charm;

import net.goo.brutality.event.forge.ServerTickHandler;
import net.goo.brutality.event.forge.client.ClientTickHandler;
import net.goo.brutality.item.curios.BrutalityMathFunctionCurio;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class Cosine extends BrutalityMathFunctionCurio {


    public Cosine(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    public static float getCurrentBonus(@Nullable Level level) {
        if (level instanceof ServerLevel) {
            return Mth.cos(ServerTickHandler.getServerTick() * 0.025f) * 0.25f + 0.125f;
        } else {
            return Mth.cos(ClientTickHandler.getClientTick() * 0.025f) * 0.25f + 0.125f;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("curios.modifiers.charm").withStyle(ChatFormatting.GOLD));

        float value = getCurrentBonus(world) * 100;

        String formattedValue = value % 1 == 0 ?
                String.format("%.0f", value) :
                String.format("%.1f", value);

        tooltip.add(Component.literal((value >= 0 ? "+" : "") + formattedValue + "% ").append(Component.translatable("attribute.name.generic.attack_speed"))
                .withStyle(value >= 0 ? ChatFormatting.BLUE : ChatFormatting.RED));

    }
}
