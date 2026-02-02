package net.goo.brutality.client.sounds;

import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class DeathsawSoundInstance extends ItemTickableSound {
    public DeathsawSoundInstance(LivingEntity user) {
        super(user, BrutalitySounds.CHAINSAW_LOOP.get());
    }

    public void tickVolume(ItemStack itemStack) {
        this.volume = 0.45F;
        this.pitch = 1;
    }

    public boolean isValidItem(ItemStack itemStack) {
        return itemStack.is(BrutalityItems.DEATHSAW.get());
    }
}
