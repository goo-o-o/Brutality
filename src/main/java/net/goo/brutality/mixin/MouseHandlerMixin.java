package net.goo.brutality.mixin;

import net.goo.brutality.registry.BrutalityMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {
    @Inject(method = "tick(ZF)V", at = @At("HEAD"), cancellable = true)
    private void lockMovement(boolean pIsSneaking, float pSneakingSpeedMultiplier, CallbackInfo ci) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.hasEffect(BrutalityMobEffects.STUNNED.get())) {
            ci.cancel();
        }
    }
}
