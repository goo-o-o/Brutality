package net.goo.brutality.mixin;

import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin {
    @Redirect(
            method = "createResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;isDamageableItem()Z"
            )
    )
    private boolean preventDoubleDownRepair(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("fromDoubleDown")) {
            return false;
        }
        return stack.isDamageableItem();
    }
}