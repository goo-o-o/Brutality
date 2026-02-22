package net.goo.brutality.mixin.mixins;

import net.goo.brutality.common.registry.BrutalityItems;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OptionInstance.class)
public class OptionInstanceMixin<T> {
    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private void shadowOptionValue(CallbackInfoReturnable<T> cir) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isHolding(BrutalityItems.OLD_GPU.get())) {
            Object instance = this;
            Options options = Minecraft.getInstance().options;

            // Check if 'this' is one of the target options
            if (instance == options.enableVsync()) {
                cir.setReturnValue((T) Boolean.FALSE);
            } else if (instance == options.ambientOcclusion()) {
                cir.setReturnValue((T) Boolean.FALSE);
            } else if (instance == options.graphicsMode()) {
                cir.setReturnValue((T) GraphicsStatus.FAST);
            } else if (instance == options.renderDistance()) {
                cir.setReturnValue((T) (Integer) 2);
            }
        }
    }
}