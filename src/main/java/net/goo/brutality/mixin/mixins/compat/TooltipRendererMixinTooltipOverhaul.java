package net.goo.brutality.mixin.mixins.compat;

import dev.xylonity.tooltipoverhaul.client.TooltipContext;
import net.goo.brutality.client.gui.tooltip.StatTrakGui;
import net.goo.brutality.util.item.StatTrakUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(targets = "dev.xylonity.tooltipoverhaul.client.TooltipRenderer", remap = false)
public abstract class TooltipRendererMixinTooltipOverhaul {

    @Shadow private static Rectangle LAST_MAIN_RECT;

    @Inject(
            method = "render",
            at = @At("TAIL") // Inject at the end to get all calculated data
    )
    private static void brutality$injectStatTrak(TooltipContext ctx, CallbackInfoReturnable<Boolean> cir) {
        // 1. Check if the render was successful (returned true)
        if (!cir.getReturnValue()) {
            return;
        }

        // 2. Get the stack from the context
        ItemStack stack = ctx.stack();
        if (StatTrakUtils.hasStatTrak(stack)) {
            GuiGraphics guiGraphics = ctx.graphics();
            StatTrakGui.render(stack, guiGraphics, LAST_MAIN_RECT.x, LAST_MAIN_RECT.y, LAST_MAIN_RECT.width, LAST_MAIN_RECT.height, 1000);
        }
    }
}