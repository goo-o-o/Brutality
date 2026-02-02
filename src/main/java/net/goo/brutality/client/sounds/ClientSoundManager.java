package net.goo.brutality.client.sounds;

import net.goo.brutality.common.block.block_entity.TableOfWizardryBlockEntity;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ClientSoundManager {
    private static final Map<BlockPos, TableOfWizardryCraftingSoundInstance> tableSounds = new HashMap<>();

    public static void handleTableOfWizardrySound(TableOfWizardryBlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        TableOfWizardryCraftingSoundInstance activeSound = tableSounds.get(pos);

        if (blockEntity.isCrafting && blockEntity.getNormalizedProgress() < 0.65F) {
            if (activeSound == null || activeSound.isStopped()) {
                activeSound = new TableOfWizardryCraftingSoundInstance(blockEntity, BrutalitySounds.TABLE_OF_WIZARDRY.get(), SoundSource.BLOCKS);
                tableSounds.put(pos, activeSound);
                Minecraft.getInstance().getSoundManager().play(activeSound);
            }
        }

        // Simple cleanup
        if (activeSound != null && activeSound.isStopped()) {
            tableSounds.remove(pos);
        }
    }
}