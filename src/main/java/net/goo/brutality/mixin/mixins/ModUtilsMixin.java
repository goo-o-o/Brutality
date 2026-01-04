package net.goo.brutality.mixin.mixins;

import net.daphne.lethality.util.ModUtils;
import net.goo.brutality.util.ColorUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModUtils.class)
public class ModUtilsMixin {

    @Inject(method = "getTickCount", at = @At("HEAD"), cancellable = true, remap = false)
    private static void fixNPE(CallbackInfoReturnable<Integer> cir) {
        cir.cancel();

        cir.setReturnValue(ColorUtils.getTickCount());
    }

}
