package net.goo.brutality.mixin.mixins;

import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.NBTUtils;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class SlotMixin {

    @Shadow
    @Final
    public Container container;

    @Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true, require = 0)
    private void restrictDoubleDown(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (NBTUtils.getBool(stack, "fromDoubleDown", false)) {
            if (ModUtils.doubleDownRestricted(container)) {
                cir.setReturnValue(false);
            }
        }
    }


}