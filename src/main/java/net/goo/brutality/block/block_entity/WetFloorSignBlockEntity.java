package net.goo.brutality.block.block_entity;

import net.goo.brutality.block.PreciseRotatableBlock;
import net.goo.brutality.registry.BrutalityModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

public class WetFloorSignBlockEntity extends BlockEntity implements PreciseRotatableBlock.RotatableBlockEntity {
    private int rotation;

    public WetFloorSignBlockEntity(BlockPos pos, BlockState state) {
        super(BrutalityModBlockEntities.WET_FLOOR_SIGN_BLOCK_ENTITY.get(), pos, state);
        this.rotation = state.getValue(BlockStateProperties.ROTATION_16);
    }



    public int getRotation() {
        return rotation;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(ROTATION_KEY, rotation);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.rotation = tag.getInt(ROTATION_KEY);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.putInt(ROTATION_KEY, rotation);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }
}