package net.goo.brutality.sounds;

import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.registry.BrutalityModSounds;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class DeathsawSoundInstance extends ItemTickableSound {
    public DeathsawSoundInstance(LivingEntity user) {
        super(user, BrutalityModSounds.CHAINSAW_LOOP.get());
    }

    public void tickVolume(ItemStack itemStack) {
        this.volume = 0.45F;
        this.pitch = 1;
    }

    public boolean isValidItem(ItemStack itemStack) {
        return itemStack.is(BrutalityModItems.DEATHSAW.get());
    }
}
