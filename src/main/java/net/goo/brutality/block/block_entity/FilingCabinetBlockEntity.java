package net.goo.brutality.block.block_entity;

import net.goo.brutality.Brutality;
import net.goo.brutality.block.FilingCabinetOpenersCounter;
import net.goo.brutality.block.custom.FilingCabinetBlock;
import net.goo.brutality.gui.menu.FilingCabinetMenu;
import net.goo.brutality.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FilingCabinetBlockEntity extends RandomizableContainerBlockEntity {
    private final int INVENTORY_SIZE = 18;
    private NonNullList<ItemStack> upperInventory = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
    private NonNullList<ItemStack> lowerInventory = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);

    private final FilingCabinetOpenersCounter openersCounter = new FilingCabinetOpenersCounter(this);

    public FilingCabinetBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FILING_CABINET_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public NonNullList<ItemStack> getUpperInventory() {
        return upperInventory;
    }

    public NonNullList<ItemStack> getLowerInventory() {
        return lowerInventory;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (!this.trySaveLootTable(pTag)) {
            saveAllItems(pTag, this.upperInventory, "UpperInventory");
            saveAllItems(pTag, this.lowerInventory, "LowerInventory");
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.upperInventory = NonNullList.withSize(this.getContainerSize() / 2, ItemStack.EMPTY);
        this.lowerInventory = NonNullList.withSize(this.getContainerSize() / 2, ItemStack.EMPTY);
        if (!this.tryLoadLootTable(pTag)) {
            loadAllItems(pTag, this.upperInventory, "UpperInventory");
            loadAllItems(pTag, this.lowerInventory, "LowerInventory");
        }
    }

    private static void loadAllItems(CompoundTag pTag, NonNullList<ItemStack> pList, String key) {
        ListTag listtag = pTag.getList(key, 10);
        for (int i = 0; i < listtag.size(); ++i) {
            CompoundTag compoundtag = listtag.getCompound(i);
            int j = compoundtag.getByte("Slot") & 255;
            if (j < pList.size()) {
                pList.set(j, ItemStack.of(compoundtag));
            }
        }
    }

    private static void saveAllItems(CompoundTag pTag, NonNullList<ItemStack> pList, String key) {
        ListTag listtag = new ListTag();
        for (int i = 0; i < pList.size(); ++i) {
            ItemStack itemstack = pList.get(i);
            if (!itemstack.isEmpty()) {
                CompoundTag compoundtag = new CompoundTag();
                compoundtag.putByte("Slot", (byte) i);
                itemstack.save(compoundtag);
                listtag.add(compoundtag);
            }
        }
        pTag.put(key, listtag);
    }

    @Override
    protected @NotNull NonNullList<ItemStack> getItems() {
        return upperInventory; // Default to upper for general access
    }

    @Override
    public @NotNull ItemStack getItem(int pIndex) {
        NonNullList<ItemStack> inventory = pIndex < INVENTORY_SIZE ? upperInventory : lowerInventory;
        return inventory.get(pIndex % INVENTORY_SIZE);
    }

    @Override
    public @NotNull ItemStack removeItem(int pIndex, int pCount) {
        this.unpackLootTable(null);
        NonNullList<ItemStack> inventory = pIndex < INVENTORY_SIZE ? upperInventory : lowerInventory;
        ItemStack stack = ContainerHelper.removeItem(inventory, pIndex % INVENTORY_SIZE, pCount);
        setChanged();
        return stack;
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {
        this.unpackLootTable(null);
        NonNullList<ItemStack> inventory = pIndex < INVENTORY_SIZE ? upperInventory : lowerInventory;
        inventory.set(pIndex % INVENTORY_SIZE, pStack);
        if (pStack.getCount() > this.getMaxStackSize()) {
            pStack.setCount(this.getMaxStackSize());
        }
        setChanged();
    }

    @Override
    public void clearContent() {
        upperInventory.clear();
        lowerInventory.clear();
        setChanged();
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int pIndex) {
        this.unpackLootTable(null);
        int relativeIndex = pIndex % INVENTORY_SIZE;
        NonNullList<ItemStack> inventory = pIndex < INVENTORY_SIZE ? upperInventory : lowerInventory;
        ItemStack stack = inventory.get(relativeIndex);
        inventory.set(relativeIndex, ItemStack.EMPTY);
        setChanged();
        return stack;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> pItemStacks) {
        if (pItemStacks.size() != INVENTORY_SIZE * 2) {
        }
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            this.upperInventory.set(i, pItemStacks.get(i));
        }
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            this.lowerInventory.set(i, pItemStacks.get(i + INVENTORY_SIZE));
        }
        setChanged();
    }

    public void startOpen(Player pPlayer, boolean isUpper) {
        if (!this.remove && !pPlayer.isSpectator() && getLevel() != null) {
            openersCounter.incrementOpeners(pPlayer, this.getLevel(), this.getBlockPos(), this.getBlockState(), isUpper);
        }
    }

    public void stopOpen(Player pPlayer, boolean isUpper) {
        if (!this.remove && !pPlayer.isSpectator() && getLevel() != null) {
            openersCounter.decrementOpeners(pPlayer, this.getLevel(), this.getBlockPos(), this.getBlockState(), isUpper);
        }
    }

    public void recheckOpen() {
        if (!this.remove && getLevel() != null) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container." + Brutality.MOD_ID + ".filing_cabinet");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new FilingCabinetMenu(pContainerId, pInventory, this, true);
    }

    @Override
    public int getContainerSize() {
        return INVENTORY_SIZE * 2;
    }

    public void updateBlockState(BlockState pState, boolean isUpper, boolean pOpen) {
        if (this.level != null) {
            this.level.setBlock(this.getBlockPos(), pState.setValue(isUpper ? FilingCabinetBlock.UPPER_OPEN : FilingCabinetBlock.LOWER_OPEN, pOpen), 3);
        }
    }
}