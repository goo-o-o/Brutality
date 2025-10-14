package net.goo.brutality.gui.menu;

import net.goo.brutality.block.block_entity.FilingCabinetBlockEntity;
import net.goo.brutality.registry.BrutalityMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import org.jetbrains.annotations.NotNull;

public class FilingCabinetMenu extends AbstractContainerMenu {
    private final FilingCabinetBlockEntity blockEntity;
    private final boolean isUpperInventory;
    private final NonNullList<ItemStack> inventory;

    public FilingCabinetMenu(int containerId, Inventory playerInventory, FilingCabinetBlockEntity blockEntity, boolean isUpperInventory) {
        super(BrutalityMenuTypes.FILING_CABINET_MENU.get(), containerId);
        this.blockEntity = blockEntity;
        this.isUpperInventory = isUpperInventory;
        this.inventory = blockEntity != null ? (isUpperInventory ? blockEntity.getUpperInventory() : blockEntity.getLowerInventory())
                : NonNullList.withSize(18, ItemStack.EMPTY);


        // Add cabinet slots (2 rows of 9 = 18 slots)
        for (int row = 0; row < 2; ++row) {
            for (int col = 0; col < 9; ++col) {
                int slotIndex = col + row * 9;
                this.addSlot(new Slot(new Container() {
                    @Override
                    public int getContainerSize() { return 18; }
                    @Override
                    public boolean isEmpty() { return inventory.stream().allMatch(ItemStack::isEmpty); }
                    @Override
                    public @NotNull ItemStack getItem(int slot) {
                        return inventory.get(slot);
                    }
                    @Override
                    public @NotNull ItemStack removeItem(int slot, int amount) {
                        ItemStack stack = ContainerHelper.removeItem(inventory, slot, amount);
                        setChanged();
                        return stack;
                    }
                    @Override
                    public @NotNull ItemStack removeItemNoUpdate(int slot) {
                        ItemStack stack = inventory.get(slot);
                        inventory.set(slot, ItemStack.EMPTY);
                        setChanged();
                        return stack;
                    }
                    @Override
                    public void setItem(int slot, ItemStack stack) {
                        inventory.set(slot, stack);
                        setChanged();
                    }
                    @Override
                    public void setChanged() {
                        if (blockEntity != null) {
                            blockEntity.setChanged();
                        }
                    }
                    @Override
                    public boolean stillValid(Player player) {
                        return blockEntity != null && blockEntity.stillValid(player);
                    }
                    @Override
                    public void clearContent() {
                        inventory.clear();
                        setChanged();
                    }
                }, slotIndex, 8 + col * 18, 18 + row * 18));
            }
        }

        // Add player inventory slots
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 67 + row * 18));
            }
        }

        // Add player hotbar slots
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 125));
        }
    }

    public FilingCabinetBlockEntity getBlockEntity() {
        return blockEntity;
    }

    @Override
    public boolean stillValid(Player player) {
        if (blockEntity == null) {
            return false;
        }
        return blockEntity.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (blockEntity == null) {
            return ItemStack.EMPTY;
        }
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 18) {
                // From cabinet to player inventory
                if (!this.moveItemStackTo(itemstack1, 18, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // From player inventory to cabinet
                if (!this.moveItemStackTo(itemstack1, 0, 18, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (blockEntity != null) {
            blockEntity.stopOpen(player, isUpperInventory);
        }
    }
}