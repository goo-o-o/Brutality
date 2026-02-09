package net.goo.brutality.mixin.mixins;

import net.goo.brutality.mixin.accessors.BrutalityFontHooks;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Font.StringRenderOutput.class})
public class StringRenderOutputMixin {
    @Shadow
    @Final
    private float dimFactor;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void brutality$captureShadow(CallbackInfo ci) {
        BrutalityFontHooks.setDimFactor(this.dimFactor);
    }

    @Inject(method = "accept", at = @At("HEAD"))
    public void brutality$captureStyle(int pos, Style style, int codePoint, CallbackInfoReturnable<Boolean> cir) {
        BrutalityFontHooks.setActiveRarity(style.getInsertion());
    }


}
