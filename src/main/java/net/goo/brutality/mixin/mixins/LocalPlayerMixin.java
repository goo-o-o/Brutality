package net.goo.brutality.mixin.mixins;

import net.goo.brutality.common.item.curios.charm.OmnidirectionalMovementGear;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.magic.AugmentHelper;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

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

        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            if (handler.isEquipped(BrutalityItems.VECTOR_STABILIZER.get())) cir.setReturnValue(true);
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
        return OmnidirectionalMovementGear.handleOmnidirectionalImpulseToMaintainSprint(localPlayer, instance);
    }

    @Redirect(method = "canStartSprinting", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"))
    private boolean bypassInitialItemUseSprintCheck(LocalPlayer instance) {
        // Have to keep in mind logic is inverted
        Optional<ICuriosItemHandler> curiosOpt = CuriosApi.getCuriosInventory(instance).resolve();
        if (curiosOpt.isPresent()) {
            ICuriosItemHandler handler = curiosOpt.get();
            if (handler.isEquipped(BrutalityItems.VECTOR_STABILIZER.get())) {
                return false;
            }
        }

        return instance.isUsingItem();
    }

    @Redirect(
            method = "aiStep",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z", ordinal = 1)
    )
    private boolean bypassContinuousItemUseSprintCheck(LocalPlayer instance) {

        Optional<ICuriosItemHandler> curiosOpt = CuriosApi.getCuriosInventory(instance).resolve();
        if (curiosOpt.isPresent()) {
            ICuriosItemHandler handler = curiosOpt.get();
            if (handler.isEquipped(BrutalityItems.VECTOR_STABILIZER.get())) return false;
        }

        return instance.isUsingItem();
    }

    @Redirect(
            method = "aiStep",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z", ordinal = 0)
    )
    private boolean bypassItemUseSlowdown(LocalPlayer instance) {
        ItemStack usedItem = instance.getUseItem();

        boolean hasBypassAugment = AugmentHelper.hasAugment(usedItem, BrutalityItems.QUICKSILVER_SPINE.get());

        if (hasBypassAugment) {
            return false;
        }

        Optional<ICuriosItemHandler> curiosOpt = CuriosApi.getCuriosInventory(instance).resolve();
        if (curiosOpt.isPresent()) {
            ICuriosItemHandler handler = curiosOpt.get();
            if (handler.isEquipped(BrutalityItems.KINETIC_COMPENSATOR.get())) return false;
        }

        return instance.isUsingItem();
    }


}
