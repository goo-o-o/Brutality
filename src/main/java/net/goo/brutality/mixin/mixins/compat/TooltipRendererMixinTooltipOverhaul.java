package net.goo.brutality.mixin.mixins.compat;

import dev.xylonity.tooltipoverhaul.client.TooltipContext;
import net.goo.brutality.common.compat.TooltipOverhaulCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(targets = "dev.xylonity.tooltipoverhaul.client.TooltipRenderer", remap = false)
public abstract class TooltipRendererMixinTooltipOverhaul {

    @Shadow
    private static Rectangle LAST_MAIN_RECT;


    @Inject(method = "render", at = @At("TAIL"), remap = false)
    private static void brutality$injectStatTrak(TooltipContext ctx, CallbackInfoReturnable<Boolean> cir) {

        // 1. Check if the render was successful (returned true)
        if (!cir.getReturnValue()) {
            return;
        }

        TooltipOverhaulCompat.handleStatTrak(ctx, LAST_MAIN_RECT);
    }
}