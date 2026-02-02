package net.goo.brutality.common.block;

import net.goo.brutality.common.block.block_entity.WhiteFilingCabinetBlockEntity;
import net.goo.brutality.client.gui.menu.FilingCabinetMenu;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.gameevent.GameEvent;

public class FilingCabinetOpenersCounter extends ContainerOpenersCounter {
    private final WhiteFilingCabinetBlockEntity blockEntity;
    private int upperOpenCount = 0;
    private int lowerOpenCount = 0;

    public FilingCabinetOpenersCounter(WhiteFilingCabinetBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    @Override
    protected void onOpen(Level level, BlockPos pos, BlockState state) {
        // Handled in incrementOpeners
    }

    @Override
    protected void onClose(Level level, BlockPos pos, BlockState state) {
        // Handled in decrementOpeners
    }

    @Override
    protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int oldCount, int newCount) {
        // Not used directly; handled by incrementOpeners/decrementOpeners
    }

    @Override
    protected boolean isOwnContainer(Player player) {
        if (player.containerMenu instanceof FilingCabinetMenu menu) {
            return menu.getBlockEntity() == blockEntity;
        }
        return false;
    }

    public void incrementOpeners(Player player, Level level, BlockPos pos, BlockState state, boolean isUpper) {
        if (isUpper) {
            upperOpenCount++;
            if (upperOpenCount == 1) {
                playSound(level, pos, SoundEvents.BARREL_OPEN);
                blockEntity.updateBlockState(state, true, true);
                level.gameEvent(player, GameEvent.CONTAINER_OPEN, pos);
            }
        } else {
            lowerOpenCount++;
            if (lowerOpenCount == 1) {
                playSound(level, pos, SoundEvents.BARREL_OPEN);
                blockEntity.updateBlockState(state, false, true);
                level.gameEvent(player, GameEvent.CONTAINER_OPEN, pos);
            }
        }
    }

    public void decrementOpeners(Player player, Level level, BlockPos pos, BlockState state, boolean isUpper) {
        if (isUpper) {
            upperOpenCount--;
            if (upperOpenCount <= 0) {
                upperOpenCount = 0;
                playSound(level, pos, SoundEvents.BARREL_CLOSE);
                blockEntity.updateBlockState(state, true, false);
                level.gameEvent(player, GameEvent.CONTAINER_CLOSE, pos);
            }
        } else {
            lowerOpenCount--;
            if (lowerOpenCount <= 0) {
                lowerOpenCount = 0;
                playSound(level, pos, SoundEvents.BARREL_CLOSE);
                blockEntity.updateBlockState(state, false, false);
                level.gameEvent(player, GameEvent.CONTAINER_CLOSE, pos);
            }
        }
    }

    private void playSound(Level level, BlockPos pos, SoundEvent sound) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        level.playSound(null, x, y, z, sound, SoundSource.BLOCKS, 0.5f, level.random.nextFloat() * 0.1f + 0.9f);
    }
}