//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.goo.brutality.sounds;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
// Thank you L_ender's Cataclysm
public abstract class ItemTickableSound extends AbstractTickableSoundInstance {
    protected final LivingEntity user;

    public ItemTickableSound(LivingEntity user, SoundEvent soundEvent) {
        super(soundEvent, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.user = user;
        this.attenuation = Attenuation.LINEAR;
        this.looping = true;
        this.x = (float)this.user.getX();
        this.y = (float)this.user.getY();
        this.z = (float)this.user.getZ();
        this.delay = 0;
    }

    public boolean canPlaySound() {
        return !this.user.isSilent() && this.user.isUsingItem() && (this.isValidItem(this.user.getItemInHand(InteractionHand.MAIN_HAND)) || this.isValidItem(this.user.getItemInHand(InteractionHand.OFF_HAND)));
    }

    public void tick() {
        ItemStack itemStack = ItemStack.EMPTY;
        if (this.user.isUsingItem()) {
            if (this.isValidItem(this.user.getItemInHand(InteractionHand.MAIN_HAND))) {
                itemStack = this.user.getItemInHand(InteractionHand.MAIN_HAND);
            }

            if (this.isValidItem(this.user.getItemInHand(InteractionHand.OFF_HAND))) {
                itemStack = this.user.getItemInHand(InteractionHand.OFF_HAND);
            }
        }

        if (this.user.isAlive() && !itemStack.isEmpty()) {
            this.x = (float)this.user.getX();
            this.y = (float)this.user.getY();
            this.z = (float)this.user.getZ();
            this.tickVolume(itemStack);
        } else {
            this.stop();
        }

    }

    protected abstract void tickVolume(ItemStack var1);

    public abstract boolean isValidItem(ItemStack var1);

    public boolean canStartSilent() {
        return true;
    }

    public boolean isSameEntity(LivingEntity user) {
        return this.user.isAlive() && this.user.getId() == user.getId();
    }
}
