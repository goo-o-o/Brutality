package net.goo.brutality.event;

import net.goo.brutality.magic.IBrutalitySpell;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public class SpellCastEvent extends Event {
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
     * Called before the {@link net.goo.brutality.magic.BrutalitySpell} is cast <br>
     * If the event is cancelled, the spell is not cast and resources will not be consumed
     */
    @Cancelable
    public static class Pre extends SpellCastEvent {
        public Pre(Player player, ItemStack stack, IBrutalitySpell spell, int spellLevel) {
            super(player, stack, spell, spellLevel);
        }
    }

    /**
     * Called after the {@link net.goo.brutality.magic.BrutalitySpell} is successfully cast <br>
     * Not Cancellable
     */
    public static class Post extends SpellCastEvent {
        public Post(Player player, ItemStack stack, IBrutalitySpell spell, int spellLevel) {
            super(player, stack, spell, spellLevel);
        }
    }
}