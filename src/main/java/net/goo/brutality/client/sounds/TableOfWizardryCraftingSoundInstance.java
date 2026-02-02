package net.goo.brutality.client.sounds;

import net.goo.brutality.common.block.block_entity.TableOfWizardryBlockEntity;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class TableOfWizardryCraftingSoundInstance extends AbstractTickableSoundInstance {
    private final TableOfWizardryBlockEntity parent;

    protected TableOfWizardryCraftingSoundInstance(TableOfWizardryBlockEntity parent, SoundEvent soundEvent, SoundSource soundSource) {
        super(soundEvent, soundSource, SoundInstance.createUnseededRandom());
        this.parent = parent;
        this.looping = true;
        this.volume = 0.01F; // Start at 0 for fade in
        this.delay = 0;
        this.x = parent.getBlockPos().getX() + 0.5f;
        this.y = parent.getBlockPos().getY() + 0.5f;
        this.z = parent.getBlockPos().getZ() + 0.5f;
    }

    @Override
    public void tick() {
        // 1. Check if we should exist at all
        // Adjust for faster/slower transitions
        float fadeSpeed = 0.05F;
        if (this.parent.isRemoved() || !this.parent.isCrafting) {
            this.volume -= fadeSpeed;
            if (this.volume <= 0) this.stop();
            return;
        }

        // 2. Dynamic Pitch (0.5 to 1.0 based on progress)
        this.pitch = 0.5F + (parent.getNormalizedProgress() * 0.5F);

        // 3. Automated Volume Logic
        float progress = parent.getNormalizedProgress();

        if (progress > 0.65F) {
            // Last 35% - Fade Out
            this.volume = Math.max(0.0F, this.volume - fadeSpeed);
            if (this.volume <= 0 && progress >= 1.0F) {
                this.stop();
            }
        } else if (progress < 0.10F) {
            // First 10% - Fade In
            this.volume = Math.min(1.0F, this.volume + fadeSpeed);
        } else {
            // Middle Loop - Maintain Full Volume
            this.volume = 1.0F;
        }

        // 4. Update position in case the player moves around the table
        this.x = parent.getBlockPos().getX() + 0.5f;
        this.y = parent.getBlockPos().getY() + 0.5f;
        this.z = parent.getBlockPos().getZ() + 0.5f;
    }

}
