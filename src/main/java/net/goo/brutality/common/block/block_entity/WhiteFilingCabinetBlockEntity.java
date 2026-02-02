package net.goo.brutality.common.block.block_entity;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.block.FilingCabinetOpenersCounter;
import net.goo.brutality.common.block.custom.WhiteFilingCabinetBlock;
import net.goo.brutality.client.gui.menu.FilingCabinetMenu;
import net.goo.brutality.common.registry.BrutalityBlockEntities;
import net.goo.brutality.util.NBTUtils;
import net.mcreator.terramity.item.CardboardItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class WhiteFilingCabinetBlockEntity extends RandomizableContainerBlockEntity {
    private final int INVENTORY_SIZE = FilingCabinetMenu.containerSize;
    private NonNullList<ItemStack> upperInventory = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
    private NonNullList<ItemStack> lowerInventory = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
    private final FilingCabinetDrawerSlideController upperDrawerController = new FilingCabinetDrawerSlideController(true);
    private final FilingCabinetDrawerSlideController lowerDrawerController = new FilingCabinetDrawerSlideController(false);
    private final FilingCabinetOpenersCounter openersCounter = new FilingCabinetOpenersCounter(this);

    public WhiteFilingCabinetBlockEntity(BlockEntityType<? extends WhiteFilingCabinetBlockEntity> entityType, BlockPos pPos, BlockState pBlockState) {
        super(entityType, pPos, pBlockState);
    }

    public static WhiteFilingCabinetBlockEntity create(BlockPos pos, BlockState state) {
        return new WhiteFilingCabinetBlockEntity(BrutalityBlockEntities.WHITE_FILING_CABINET_BLOCK_ENTITY.get(), pos, state);
    }

    public NonNullList<ItemStack> getUpperInventory() {
        return upperInventory;
    }

    public NonNullList<ItemStack> getLowerInventory() {
        return lowerInventory;
    }

    public float getOpenness(boolean isUpper, float partialTicks) {
        return isUpper ? upperDrawerController.getOpenness(partialTicks) : lowerDrawerController.getOpenness(partialTicks);
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
    public boolean canPlaceItem(int pIndex, ItemStack pStack) {
        return (pStack.is(Items.PAPER) || pStack.is(ItemTags.LECTERN_BOOKS) ||
                pStack.is(Items.ENCHANTED_BOOK) || pStack.is(Items.BOOK) || pStack.getItem() instanceof CardboardItem) && !NBTUtils.getBool(pStack, "fromDoubleDown", false);
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

    public static <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? createTickerHelper(blockEntityType, BrutalityBlockEntities.WHITE_FILING_CABINET_BLOCK_ENTITY.get(), WhiteFilingCabinetBlockEntity::tickDrawer) : null;
    }

    @SuppressWarnings("unchecked")
    static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker) {
        return clientType == serverType ? (BlockEntityTicker<A>) ticker : null;
    }

    static void tickDrawer(Level level, BlockPos pos, BlockState state, WhiteFilingCabinetBlockEntity blockEntity) {
        blockEntity.upperDrawerController.tickDrawer(state.getValue(WhiteFilingCabinetBlock.UPPER_OPEN));
        blockEntity.lowerDrawerController.tickDrawer(state.getValue(WhiteFilingCabinetBlock.LOWER_OPEN));
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
            this.level.setBlock(this.getBlockPos(), pState.setValue(isUpper ? WhiteFilingCabinetBlock.UPPER_OPEN : WhiteFilingCabinetBlock.LOWER_OPEN, pOpen), 3);
        }
    }
}