package net.goo.brutality.mixin;

import net.goo.brutality.util.helpers.NbtHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemHandlerHelper.class)
public class ItemHandlerHelperMixin {

    @Inject(
            method = "insertItem",
            at = @At("HEAD"),
            cancellable = true, remap = false
    )
    private static void preventDoubleDownInsert(
            IItemHandler handler, ItemStack stack, boolean simulate,
            CallbackInfoReturnable<ItemStack> cir
    ) {
        if (!stack.isEmpty() && NbtHelper.getBool(stack, "fromDoubleDown", false)) {
            cir.setReturnValue(stack.copy()); // Nothing inserted
        }
    }
}