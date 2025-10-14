package net.goo.brutality.gui.menu;

import com.teamabnormals.environmental.common.entity.animal.slabfish.Slabfish;
import com.teamabnormals.environmental.common.inventory.SlabfishInventory;
import net.goo.brutality.registry.BrutalityMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.goo.brutality.block.block_entity.FilingCabinetBlockEntity;
import org.jetbrains.annotations.NotNull;

public class FilingCabinetMenu extends AbstractContainerMenu {
    private final FilingCabinetBlockEntity blockEntity;
    private final boolean isUpperInventory;
    private static final int INVENTORY_SIZE = 18;

    public FilingCabinetMenu(int containerId, Inventory playerInventory, FilingCabinetBlockEntity blockEntity, boolean isUpperInventory) {
        super(BrutalityMenuTypes.FILING_CABINET_MENU.get(), containerId);
        this.blockEntity = blockEntity;
        this.isUpperInventory = isUpperInventory;

        // Add selected inventory slots (27 slots, 3x9 grid)
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 9; col++) {
                int slotIndex = col + row * 9;
                this.addSlot(new Slot(blockEntity, slotIndex + (isUpperInventory ? 0 : INVENTORY_SIZE), 8 + col * 18, 18 + row * 18));
            }
        }

        // Add player inventory slots
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Add player hotbar slots
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    public FilingCabinetMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, null, true);

    }

    public FilingCabinetBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public boolean isUpperInventory() {
        return isUpperInventory;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide) {
            blockEntity.stopOpen(player, isUpperInventory);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.stillValid(player);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();

            // From cabinet to player inventory
            if (index < INVENTORY_SIZE) {
                if (!this.moveItemStackTo(slotStack, INVENTORY_SIZE, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            // From player inventory to cabinet
            else if (!this.moveItemStackTo(slotStack, 0, INVENTORY_SIZE, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return stack;
    }
}