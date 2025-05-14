package net.goo.armament.mixin;

import net.goo.armament.util.helpers.CameraHelper;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class CameraLockMixin {
    @Shadow private double accumulatedDX;

    @Shadow private double accumulatedDY;

    @Inject(method = "turnPlayer", at = @At("HEAD"), cancellable = true)
    public void onMouseMovement(CallbackInfo ci) {
        if (CameraHelper.isCameraLocked()) {
            this.accumulatedDX = 0;
            this.accumulatedDY = 0;

            ci.cancel();
        }
    }
}
