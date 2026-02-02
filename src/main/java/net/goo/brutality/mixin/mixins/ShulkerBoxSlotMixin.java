package net.goo.brutality.mixin.mixins;

import net.goo.brutality.util.NBTUtils;
import net.minecraft.world.inventory.ShulkerBoxSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShulkerBoxSlot.class)
public class ShulkerBoxSlotMixin {

    @Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
    private void restrictDoubleDown(ItemStack pStack, CallbackInfoReturnable<Boolean> cir) {
        if (NBTUtils.getBool(pStack, "fromDoubleDown", false)) {
            cir.setReturnValue(false);
        }
    }
}
