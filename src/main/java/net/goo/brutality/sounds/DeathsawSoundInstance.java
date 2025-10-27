package net.goo.brutality.sounds;

import com.github.L_Ender.cataclysm.init.ModSounds;
import net.goo.brutality.registry.BrutalityModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class DeathsawSoundInstance extends ItemTickableSound {
    public DeathsawSoundInstance(LivingEntity user) {
        super(user, ModSounds.SHREDDER_LOOP.get());
    }

    public void tickVolume(ItemStack itemStack) {
        this.volume = 0.4F;
        this.pitch = 1.0F;
    }

    public boolean isValidItem(ItemStack itemStack) {
        return itemStack.is(BrutalityModItems.DEATHSAW.get());
    }
}
