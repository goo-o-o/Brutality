package net.goo.brutality.client.event.forge;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.datafixers.util.Either;
import net.goo.brutality.Brutality;
import net.goo.brutality.util.AugmentHelper;
import net.goo.brutality.util.tooltip.ItemAugmentComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeClientTooltipEvents {


    // Move tooltipImage for magic items to the bottom
    @SubscribeEvent
    public static void onGatherComponents(RenderTooltipEvent.GatherComponents event) {
        renderAugmentSlots(event);
    }

    // instead of using getTooltipImage, we directly modify in the tooltip event so that we can easier control where it is located
    @OnlyIn(Dist.CLIENT)
    public static void renderAugmentSlots(RenderTooltipEvent.GatherComponents event) {
        Minecraft mc = Minecraft.getInstance();
        List<Either<FormattedText, TooltipComponent>> elements = event.getTooltipElements();
        ItemStack stack = event.getItemStack();
        if (!InputConstants.isKeyDown(mc.getWindow().getWindow(), mc.options.keyShift.getKey().getValue())) return;

        // 2. Find the insertion point
        int insertionIndex = -1;
        for (int i = 0; i < elements.size(); i++) {
            var either = elements.get(i);
            if (either.left().isPresent()) {
                // We look for the "Press Shift" text you added in appendHoverText
                // Or look for the start of modifiers
                String text = either.left().get().getString();

                // This is the most reliable way: insert right before the modifiers start
                // Note: Modifier headers often use translatable keys, so we check the raw content
                if (text.contains("item.modifiers") || text.contains("When in") || text.isEmpty() && i > elements.size() - 3) {
                    insertionIndex = i;
                    break;
                }
            }
        }

        // 3. Create and Inject the Component
        // If we didn't find a modifier header, insertionIndex remains -1, we'll just put it at the end
        if (insertionIndex == -1) {
            insertionIndex = elements.size();
        }

        var data = new ItemAugmentComponent.AugmentComponent(stack, AugmentHelper.getAugmentsFromItem(stack), AugmentHelper.getAugmentSlots(stack));
        elements.add(insertionIndex, Either.right(data));
        elements.add(insertionIndex + 1, Either.left(Component.empty()));
    }
}
