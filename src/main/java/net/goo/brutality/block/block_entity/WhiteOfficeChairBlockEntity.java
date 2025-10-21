package net.goo.brutality.block.block_entity;

import net.goo.brutality.block.PreciseRotatableBlock;
import net.goo.brutality.registry.BrutalityModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

public class WhiteOfficeChairBlockEntity extends BlockEntity implements PreciseRotatableBlock.RotatableBlockEntity {
    private int rotation;

    public WhiteOfficeChairBlockEntity(BlockEntityType<? extends WhiteOfficeChairBlockEntity> entityType, BlockPos pos, BlockState state) {
        super(entityType, pos, state);
        rotation = state.getValue(BlockStateProperties.ROTATION_16);
    }

    public static WhiteOfficeChairBlockEntity create(BlockPos pos, BlockState state) {
        return new WhiteOfficeChairBlockEntity(BrutalityModBlockEntities.WHITE_OFFICE_CHAIR_BLOCK_ENTITY.get(), pos, state);
    }


    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(ROTATION_KEY, rotation);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        rotation = tag.getInt(ROTATION_KEY);
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

    @Override
    public int getRotation() {
        return rotation;
    }
}