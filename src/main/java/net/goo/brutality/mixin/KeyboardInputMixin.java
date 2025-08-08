package net.goo.brutality.mixin;

import net.goo.brutality.registry.BrutalityModMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {
    @Inject(method = "tick(ZF)V", at = @At("HEAD"), cancellable = true)
    private void lockMovement(boolean pIsSneaking, float pSneakingSpeedMultiplier, CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;
        if (player != null &&
                (player.hasEffect(BrutalityModMobEffects.STUNNED.get()) || player.hasEffect(BrutalityModMobEffects.LIGHT_BOUND.get()))
        ) {
            ci.cancel();
        }
    }
}
