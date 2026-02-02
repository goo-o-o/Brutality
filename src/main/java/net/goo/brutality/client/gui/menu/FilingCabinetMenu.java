package net.goo.brutality.client.gui.menu;

import net.goo.brutality.common.block.block_entity.WhiteFilingCabinetBlockEntity;
import net.goo.brutality.common.registry.BrutalityMenuTypes;
import net.goo.brutality.common.registry.BrutalityItems;
import net.mcreator.terramity.item.CardboardItem;
import net.minecraft.core.NonNullList;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class FilingCabinetMenu extends AbstractContainerMenu {
    private final WhiteFilingCabinetBlockEntity blockEntity;
    private final boolean isUpperInventory;
    private final NonNullList<ItemStack> inventory;

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public static final int rowCount = 6;
    public static final int columnCount = 7;
    public static final int containerSize = rowCount * columnCount;

    public FilingCabinetMenu(int containerId, Inventory playerInventory, WhiteFilingCabinetBlockEntity blockEntity, boolean isUpperInventory) {
        super(BrutalityMenuTypes.FILING_CABINET_MENU.get(), containerId);
        this.blockEntity = blockEntity;
        this.isUpperInventory = isUpperInventory;
        this.inventory = blockEntity != null ? (isUpperInventory ? blockEntity.getUpperInventory() : blockEntity.getLowerInventory())
                : NonNullList.withSize(18, ItemStack.EMPTY);


        // Add cabinet slots (2 rows of 9 = 18 slots)
        for (int row = 0; row < rowCount; ++row) {
            for (int col = 0; col < columnCount; ++col) {
                int slotIndex = col + row * columnCount;
                this.addSlot(new Slot(


                        new Container() {
                            @Override
                            public int getContainerSize() {
                                return containerSize;
                            }

                            @Override
                            public boolean isEmpty() {
                                return inventory.stream().allMatch(ItemStack::isEmpty);
                            }

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
                            public boolean canPlaceItem(int pIndex, ItemStack pStack) {
                                return pStack.is(Items.PAPER) || pStack.is(ItemTags.LECTERN_BOOKS) || pStack.is(BrutalityItems.IMPORTANT_DOCUMENTS.get()) ||
                                        pStack.is(Items.ENCHANTED_BOOK) || pStack.is(Items.BOOK) || pStack.getItem() instanceof CardboardItem;
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
                        }, slotIndex, 8 + col * 18 + 18, 18 + row * 18) {
                    @Override
                    public boolean mayPlace(ItemStack pStack) {
                        return pStack.is(Items.PAPER) || pStack.is(ItemTags.LECTERN_BOOKS) ||
                                pStack.is(Items.ENCHANTED_BOOK) || pStack.is(Items.BOOK) || pStack.getItem() instanceof CardboardItem;
                    }
                });
            }
        }
        int i = (this.getRowCount() - 4) * 18;

        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 161 + i));
        }

    }

    public WhiteFilingCabinetBlockEntity getBlockEntity() {
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
            if (index < containerSize) {
                // From cabinet to player inventory
                if (!this.moveItemStackTo(itemstack1, containerSize, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // From player inventory to cabinet
                if (!this.moveItemStackTo(itemstack1, 0, containerSize, false)) {
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