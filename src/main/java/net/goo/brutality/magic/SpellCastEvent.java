package net.goo.brutality.magic;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
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

    @Cancelable
    public static class Server extends SpellCastEvent {
        public Server(Player player, ItemStack stack, IBrutalitySpell spell, int spellLevel) {
            super(player, stack, spell, spellLevel);
        }
    }

    public static class Client extends SpellCastEvent {
        public Client(Player player, ItemStack stack, IBrutalitySpell spell, int spellLevel) {
            super(player, stack, spell, spellLevel);
        }
    }
}