package net.goo.brutality.mixin;

import net.goo.brutality.gui.PocketBlackHoleSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin extends AbstractContainerMenu {
    protected InventoryMenuMixin(MenuType<?> pMenuType, int pContainerId) {
        super(pMenuType, pContainerId);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void addExtraSlots(Inventory pPlayerInventory, boolean pActive, Player pOwner, CallbackInfo ci) {

        this.addSlot(new PocketBlackHoleSlot(pPlayerInventory, 50, 200, 50));

    }
}