package net.goo.brutality.sounds;

import net.goo.brutality.entity.spells.brimwielder.ExtinctionEntity;
import net.goo.brutality.registry.BrutalityModSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class ExtinctionSpellSoundInstance extends AbstractTickableSoundInstance {
    private final Entity entity;

    public ExtinctionSpellSoundInstance(ExtinctionEntity entity) {
        super(BrutalityModSounds.LASER_BEAM.get(), SoundSource.AMBIENT, SoundInstance.createUnseededRandom());
        this.entity = entity;
        this.delay = 0;
        this.looping = true;
        this.volume = Mth.clamp(entity.getSpellLevel() / 10F, 0F, 1F);
        this.pitch = 1;
        this.x = entity.getX();
        this.y = entity.getY();
        this.z = entity.getZ();
        this.attenuation = Attenuation.LINEAR;
    }

    @Override
    public void tick() {
        if (!entity.isAlive()) {
            stop();
            return;
        }
        this.x = (float) this.entity.getX();
        this.y = (float) this.entity.getY();
        this.z = (float) this.entity.getZ();
    }
}
