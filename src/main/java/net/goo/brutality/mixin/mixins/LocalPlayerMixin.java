package net.goo.brutality.mixin.mixins;

import net.goo.brutality.registry.BrutalityModItems;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    // Recommended structure for the Inject
    @Inject(method = "hasEnoughImpulseToStartSprinting", at = @At("HEAD"), cancellable = true)
    private void handleOmniSprintStart(CallbackInfoReturnable<Boolean> cir) {
        LocalPlayer player = (((LocalPlayer) (Object) this));

        if (player.isUnderWater()) {
            cir.setReturnValue(player.input.hasForwardImpulse());
            return;
        }

        // Check for Curios inventory and run the logic if present
        CuriosApi.getCuriosInventory(player).resolve().ifPresent(handler -> {
            if (handler.isEquipped(BrutalityModItems.OMNIDIRECTIONAL_MOVEMENT_GEAR.get())) {
                boolean canSprint = Math.abs(player.input.forwardImpulse) != 0 || Math.abs(player.input.leftImpulse) != 0;
                cir.setReturnValue(canSprint);
                cir.cancel();
            }
        });
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

        return CuriosApi.getCuriosInventory(localPlayer).resolve().map(handler -> {
            return handler.isEquipped(BrutalityModItems.OMNIDIRECTIONAL_MOVEMENT_GEAR.get())
                    ? (instance.forwardImpulse != 0.0F || instance.leftImpulse != 0.0F) // Omni-sprint logic
                    : (instance.forwardImpulse > 1.0E-5F);                              // Normal sprint logic (Original forward-only check)
        }).orElseGet(() -> instance.forwardImpulse > 1.0E-5F);
    }

}
