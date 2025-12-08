package net.goo.brutality.mixin;

import net.goo.brutality.util.helpers.NbtHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.VanillaHopperItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VanillaHopperItemHandler.class)
public class VanillaHopperItemHandlerMixin {
    @Inject(method = "insertItem", at = @At("HEAD"), cancellable = true, remap = false)
    private void blockDoubleDown(int slot, ItemStack stack, boolean simulate, CallbackInfoReturnable<ItemStack> cir){
        if (NbtHelper.getBool(stack, "fromDoubleDown", false)) {
            cir.setReturnValue(stack);
        }
    }
}
