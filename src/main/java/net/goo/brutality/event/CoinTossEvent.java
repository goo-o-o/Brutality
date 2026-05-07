package net.goo.brutality.event;

import net.goo.brutality.common.item.base.BrutalityCoinItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

import javax.annotation.Nullable;

public class CoinTossEvent extends Event implements IModBusEvent {
    private final Player player;
    private final ItemStack coinStack;
    private Vec3 spawnPos, launchVelocity, angularVelocity;

    /**
     * Represents an event triggered when a player performs a coin toss action.
     *
     * @param player           The {@link Player} who initiated the coin toss.
     * @param coinStack        The {@link ItemStack} representing the coin being tossed.
     * @param spawnPos         A {@link Vec3} indicating the initial spawn position of the tossed coin.
     * @param launchVelocity   A {@link Vec3} defining the velocity at which the coin is launched.
     * @param angularVelocity  A {@link Vec3} specifying the angular velocity of the coin during the toss.
     */
    public CoinTossEvent(Player player, ItemStack coinStack, Vec3 spawnPos, Vec3 launchVelocity, Vec3 angularVelocity) {
        this.player = player;
        this.coinStack = coinStack;
        this.spawnPos = spawnPos;
        this.launchVelocity = launchVelocity;
        this.angularVelocity = angularVelocity;
    }

    public Vec3 getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(Vec3 angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    @Nullable
    public BrutalityCoinItem getCoinItem() {
        if (getCoinStack().getItem() instanceof BrutalityCoinItem coinItem)
            return coinItem;
        return null;
    }

    public ItemStack getCoinStack() {
        return coinStack;
    }

    public Vec3 getLaunchVelocity() {
        return launchVelocity;
    }

    public void setLaunchVelocity(Vec3 launchVelocity) {
        this.launchVelocity = launchVelocity;
    }

    public Player getPlayer() {
        return player;
    }

    public Vec3 getSpawnPos() {
        return spawnPos;
    }

    public void setSpawnPos(Vec3 spawnPos) {
        this.spawnPos = spawnPos;
    }
}
