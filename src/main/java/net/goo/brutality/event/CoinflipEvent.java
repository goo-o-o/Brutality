package net.goo.brutality.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

public class CoinflipEvent extends Event implements IModBusEvent {
    private final Player player;
    private final ItemStack coinStack;
    private final Vec3 landPos;
    private CoinflipResult flipResult;

    /**
     * Represents an event triggered when a coin toss action completes, indicating the outcome
     * and where the coin landed.
     *
     * @param player      The {@link Player} who tossed the coin.
     * @param coinStack   The {@link ItemStack} representing the coin used in the toss.
     * @param landPos     A {@link Vec3} indicating the position where the coin ultimately landed.
     * @param flipResult  The {@link CoinflipResult} indicating the outcome of the toss
     *                    (e.g., {@code HEADS}, {@code TAILS}, or {@code EDGE}).
     */
    public CoinflipEvent(Player player, ItemStack coinStack, Vec3 landPos, CoinflipResult flipResult) {
        this.player = player;
        this.coinStack = coinStack;
        this.landPos = landPos;
        this.flipResult = flipResult;
    }

    public CoinflipResult getFlipResult() {
        return flipResult;
    }

    public void setFlipResult(CoinflipResult flipResult) {
        this.flipResult = flipResult;
    }

    public ItemStack getCoinStack() {
        return coinStack;
    }

    public Vec3 getLandPos() {
        return landPos;
    }

    public Player getPlayer() {
        return player;
    }

    public enum CoinflipResult {
        HEADS, TAILS, EDGE
    }
}
