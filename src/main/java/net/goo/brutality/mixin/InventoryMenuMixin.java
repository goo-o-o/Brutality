//package net.goo.brutality.mixin;
//
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.AbstractContainerMenu;
//import net.minecraft.world.inventory.InventoryMenu;
//import net.minecraft.world.inventory.MenuType;
//import net.minecraft.world.inventory.Slot;
//import org.jetbrains.annotations.Nullable;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(InventoryMenu.class)
//public abstract class InventoryMenuMixin extends AbstractContainerMenu{
//    protected InventoryMenuMixin(@Nullable MenuType<?> pMenuType, int pContainerId) {
//        super(pMenuType, pContainerId);
//    }
//
//    @Inject(method = "<init>", at = @At("RETURN"))
//    private void modifyTopRightInventorySlot(Inventory inventory, boolean active, Player player, CallbackInfo ci) {
//        int indexToReplace = 17;
//        Slot original = slots.get(indexToReplace);
//
//        Slot customSlot = new Slot(inventory, original.index, original.x, original.y) {
//            @Override
//            public int getMaxStackSize() {
//                return 1024; // your custom max stack size
//            }
//        };
//
//        customSlot.setBackground(InventoryMenu.BLOCK_ATLAS, new ResourceLocation("brutality", "gui/pocket_black_hole"));
//        this.broadcastChanges();
//        slots.set(indexToReplace, customSlot);
//    }
//}
