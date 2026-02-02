package net.goo.brutality.common.block.block_entity;

import net.goo.brutality.common.registry.BrutalityBlockEntities;
import net.goo.brutality.common.registry.BrutalityBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PedestalOfWizardryBlockEntity extends BlockEntity {
    private ItemStack storedItem = ItemStack.EMPTY;

    public PedestalOfWizardryBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BrutalityBlockEntities.PEDESTAL_OF_WIZARDRY_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public ItemStack getStoredItem() {
        return this.storedItem;
    }

    public BlockPos getTableOfWizardryPosition() {
        Level level = this.getLevel();
        int radius = 3;
        int r = (int) (double) radius;
        if (level == null) return null;
        BlockPos center = this.worldPosition;
        for (int x = -r; x <= r; x++) {
            for (int z = -r; z <= r; z++) {
                BlockPos pos = center.offset(x, 0, z);

                if (pos.equals(center)) continue;

                // Euclidean distance
                double dist = Math.sqrt( x * x + z * z );

                if (dist <= radius && level.getBlockState(pos).is(BrutalityBlocks.TABLE_OF_WIZARDRY.get())) {
                    return pos;
                }
            }
        }
        return null;
    }

    public void setStoredItem(ItemStack stack) {
        this.storedItem = stack;
        this.setChanged(); // Marks the block for saving to disk
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (!this.storedItem.isEmpty()) {
            pTag.put("StoredItem", this.storedItem.save(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("StoredItem", 10)) {
            this.storedItem = ItemStack.of(pTag.getCompound("StoredItem"));
        }
    }


    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }
}