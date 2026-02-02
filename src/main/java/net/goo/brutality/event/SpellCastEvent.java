package net.goo.brutality.event;

import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.magic.IBrutalitySpell;
import net.goo.brutality.util.magic.SpellCastingHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

/**
 * Called on the Mod Bus
 */
public class SpellCastEvent extends Event implements IModBusEvent {
    private final Player player;
    private final ItemStack stack;
    private final int spellLevel;
    private final IBrutalitySpell spell;

    public SpellCastEvent(Player player, ItemStack stack, IBrutalitySpell spell, int spellLevel) {
        this.player = player;
        this.stack = stack;
        this.spellLevel = spellLevel;
        this.spell = spell;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getStack() {
        return stack;
    }

    public int getSpellLevel() {
        return spellLevel;
    }

    public IBrutalitySpell getSpell() {
        return spell;
    }

    /**
     * Called before the {@link BrutalitySpell} is cast through the {@link SpellCastingHandler} <br>
     * If the event is cancelled, the spell is not cast and resources will not be consumed
     */
    @Cancelable
    public static class Pre extends SpellCastEvent {
        public Pre(Player player, ItemStack stack, IBrutalitySpell spell, int spellLevel) {
            super(player, stack, spell, spellLevel);
        }
    }

    /**
     * Called after the {@link BrutalitySpell} is successfully cast through the {@link SpellCastingHandler} <br>
     * Not Cancellable
     */
    public static class Post extends SpellCastEvent {
        public Post(Player player, ItemStack stack, IBrutalitySpell spell, int spellLevel) {
            super(player, stack, spell, spellLevel);
        }
    }
}