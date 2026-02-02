package net.goo.brutality.mixin.mixins;

import net.goo.brutality.common.item.curios.charm.OmnidirectionalMovementGear;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Inject(method = "hasEnoughImpulseToStartSprinting", at = @At("HEAD"), cancellable = true)
    private void handleOmniSprintStart(CallbackInfoReturnable<Boolean> cir) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        // Emulate vanilla logic
        if (player.isUnderWater()) {
            cir.setReturnValue(player.input.hasForwardImpulse());
            return;
        }

        OmnidirectionalMovementGear.getOmnidirectionalImpulse(player).ifPresent(cir::setReturnValue);
    }

    @Redirect(
            method = "aiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/Input;hasForwardImpulse()Z"
            )
    )
    private boolean checkAnyHorizontalImpulseToMaintainSprint(Input instance) {
        LocalPlayer localPlayer = (((LocalPlayer) (Object) this));
        return OmnidirectionalMovementGear.handleOmnidirectionalImpulseToMaintainSprint(localPlayer, instance);
    }

}
