package net.goo.brutality.mixin.mixins;

import net.goo.brutality.common.registry.BrutalityEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Inject(method = "turnPlayer()V", at = @At("HEAD"), cancellable = true)
    private void lockMovement(CallbackInfo ci) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.hasEffect(BrutalityEffects.STUNNED.get())) {
            if (Minecraft.getInstance().player.isAlive()) {
                ci.cancel();
            }
        }
    }
}
